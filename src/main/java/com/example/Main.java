package com.example;

import io.prometheus.metrics.core.metrics.Counter;
import io.prometheus.metrics.exporter.httpserver.HTTPServer;
import io.prometheus.metrics.model.registry.PrometheusRegistry;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        Main main = new Main();
        main.run();
    }

    private void run() throws Exception {

        Counter counter1 = createCounter("counter1");
        Counter counter2 = createCounter("counter2", Arrays.asList("label1", "label2"));

        counter1.inc();
        counter2.labelValues("a", "b").inc();

        PrometheusRegistry.defaultRegistry.register(counter1);
        PrometheusRegistry.defaultRegistry.register(counter2);

        HTTPServer server = HTTPServer.builder().port(9400).buildAndStart();
        System.out.println("HTTPServer listening on port http://localhost:" + server.getPort() + "/metrics");
        Thread.currentThread().join(); // wait forever
    }

    private Counter createCounter(String name) {
        Counter.Builder builder = Counter.builder();
        builder.name(name);
        return builder.build();
    }

    private Counter createCounter(String name, List<String> labels) {
        Counter.Builder builder = Counter.builder();
        builder.name(name);
        if (labels != null && !labels.isEmpty()) {
            builder.labelNames(labels.stream().toArray(String[]::new));
        }
        return builder.build();
    }
}