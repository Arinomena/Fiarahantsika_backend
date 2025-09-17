package com.fiarahantsika.backend.common.service;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import prediction.PredictionServiceGrpc;
import prediction.Prediction.ForecastRequest;
import prediction.Prediction.ForecastResponse;
import prediction.Prediction.MetricsRequest;
import prediction.Prediction.MetricsResponse;

@Service
public class PredictionGrpcClient {

    @GrpcClient("predictionService")
    private PredictionServiceGrpc.PredictionServiceBlockingStub stub;

    public ForecastResponse getForecast(int productId, int periods) {
        ForecastRequest req = ForecastRequest.newBuilder()
                .setProductId(productId)
                .setPeriods(periods)
                .build();
        return stub.forecast(req);
    }

    public MetricsResponse getMetrics(Integer top, Integer flop) {
        MetricsRequest.Builder builder = MetricsRequest.newBuilder();
        if (top != null) builder.setTop(top);
        if (flop != null) builder.setFlop(flop);
        return stub.metrics(builder.build());
    }
}
