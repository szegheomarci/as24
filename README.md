# Car Ads
This app collects data from car classifieds and exports them in the desired 
formats.

## Configuration
The input is configured by a datasource value: a site where cars are advertized
and the list of searches we are interested in.  
The output is configured in a list with each item representing a method and its
configuration.

To configure the app, create your `./config/config.yaml` file.

*Example*
```yaml
datasource:
  reader: AUTOSCOUTREADER
  urls:
    - "https://www.autoscout24.com/lst/ferrari/308?&fregfrom=1981&fregto=1985&ustate=N%2CU&size=20&page="
    - "https://www.autoscout24.com/lst/ferrari/monza?atype=C&page="
output:
  - type: file
    format: dsv
    delimiter: "####"
  - type: file
    format: json
  - type: kafka
    config:
      bootstrapServers: your.server.cloudkafka.com:9094
      username: yourusername
      password: yourPa$$word
      topic: your-topic-name
```
### Datasource
Only one datasource can be defined in the config file.  
Only AUTOSCOUTREADER is supported.

#### AUTOSCOUTREADER
This datasource reader collects info from `www.autoscout24.com`. The URLs for 
the search must be provided in a list. The URL must end with `page=`. Remember,
this site supports a maximum of 400 returned results. So filter your search 
accordingly.

### Output
One or multiple outputs can be defined in the config file. There are multiple
options with individual configurations available.  
The required output methods must be listed under `output:` in the config file. 

#### Create a file
This output type writes the results in a text based file. The following formats are available:

Delimiter Separated values
: Similar to csv, but the delimiter is customized. Use the following configuration:  

```yaml
- type: file  
  format: dsv  
  delimiter: "<your delimiter>"
```
json
: Output the results in a json file. Use the following configuration:

```yaml
- type: file
  format: json
```

#### Write to a Kafka topic
This output type writes the results to a Kafka topic.  
The following configuration parameters are mandatory:

```yaml
- type: kafka
  config:
    bootstrapServers: your.server.cloudkafka.com:9094
    username: yourusername
    password: yourPa$$word
    topic: your-topic-name
```
The following configuration parameters are optional:  
`keySerializer:` custom *key.serializer*
`valueSerializer:` custom *value.serializer*
`valueSerializer:` custom *value.serializer*

        properties.put("value.serializer", valueSerializer);
        properties.put("security.protocol", securityProtocol);
        properties.put("sasl.mechanism", saslMechanism);
        properties.put("sasl.jaas.config", saslJaasConfig);
        properties.put("enable.idempotence", "false");
```yaml
    keySerializer:
    valueSerializer:
    securityProtocol:
    saslMechanism:
    jaasTemplate:
```