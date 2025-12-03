package com.fiarahantsika.backend.common.controller;

import com.fiarahantsika.backend.common.service.PredictionGrpcClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import prediction.Prediction.ForecastResponse;
import prediction.Prediction.MetricsResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prediction")
@RequiredArgsConstructor
public class PredictionController {

    private final PredictionGrpcClient predictionGrpcClient;

    @GetMapping("/forecast")
    public Map<String, Object> getForecast(
            @RequestParam int productId,
            @RequestParam(defaultValue = "30") int periods) {

        ForecastResponse res = predictionGrpcClient.getForecast(productId, periods);

        List<Map<String, Object>> forecastsList = res.getForecastsList().stream()
                .map(item -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("date", item.getDate());
                    map.put("yhat", item.getYhat());
                    map.put("yhatLower", item.getYhatLower());
                    map.put("yhatUpper", item.getYhatUpper());
                    return map;
                })
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("raw", forecastsList);

        if (forecastsList.isEmpty()) {
            result.put("readable", "Aucune prévision disponible pour ce produit.");
            return result;
        }

        String readable = forecastsList.stream()
                .map(item -> "- Le " + item.get("date")
                        + " : environ " + Math.round((Double) item.get("yhat"))
                        + " unités (fourchette : "
                        + Math.round((Double) item.get("yhatLower")) + " à "
                        + Math.round((Double) item.get("yhatUpper")) + ")")
                .collect(Collectors.joining("\n",
                        "Prévisions pour les " + periods + " prochains jours :\n", ""));

        result.put("readable", readable);
        return result;
    }

    @GetMapping("/metrics")
    public Map<String, Object> getMetrics(
            @RequestParam(required = false) Integer top,
            @RequestParam(required = false) Integer flop) {

        MetricsResponse res = predictionGrpcClient.getMetrics(top, flop);

        List<Map<String, Object>> metricsList = res.getMetricsList().stream()
                .map(m -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("productId", m.getProductId());
                    map.put("productName", m.getProductName());
                    map.put("mae", m.getMae());
                    map.put("rmse", m.getRmse());
                    return map;
                })
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("raw", metricsList);

        if (metricsList.isEmpty()) {
            result.put("readable", "Aucune métrique disponible.");
            return result;
        }

        String titre = (top != null) ? "TOP " + top + " produits les plus précis"
                : (flop != null) ? "TOP " + flop + " produits les moins précis"
                : "Métriques de précision pour tous les produits";

        String details = metricsList.stream()
                .map(m -> "- " + m.get("productName")
                        + " : erreur moyenne ±" + m.get("mae")
                        + " unités, RMSE " + m.get("rmse"))
                .collect(Collectors.joining("\n"));

        result.put("readable", titre + " :\n" + details);
        return result;
    }
}
