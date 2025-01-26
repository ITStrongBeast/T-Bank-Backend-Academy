# Benchmark Results

| Benchmark                              | Mode | Cnt | Score (ns/op) | Error (ns/op) | Units |
|----------------------------------------|------|-----|---------------|---------------|-------|
| ReflectionBenchmark.directAccess       | avgt |   5 | 0.379         | ± 0.002       | ns/op |
| ReflectionBenchmark.lambdaMetafactory  | avgt |   5 | 0.493         | ± 0.003       | ns/op |
| ReflectionBenchmark.methodHandle       | avgt |   5 | 2.423         | ± 0.014       | ns/op |
| ReflectionBenchmark.reflection         | avgt |   5 | 3.764         | ± 0.036       | ns/op |
