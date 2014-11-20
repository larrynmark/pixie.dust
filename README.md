pixie.dust
==========
This is an agent, which outputs the name of the method that is performed in the Application.

Build
==========
mvn clean assembly:assembly

Usage
==========
Add to your application of the instruction as follows :
"-javaagent:pixie-dust-jar-with-dependencies.jar"

Output Example
==========
[### PIXIE DUST ### org/springframework/data/redis/core/RedisTemplate postProcessResult (Ljava/lang/Object;Lorg/springframework/data/redis/connection/RedisConnection;Z)Ljava/lang/Object; 176
[### PIXIE DUST ### org/springframework/data/redis/core/RedisConnectionUtils isConnectionTransactional (Lorg/springframework/data/redis/connection/RedisConnection;Lorg/springframework/data/redis/connection/RedisConnectionFactory;)Z 172
[### PIXIE DUST ### org/springframework/data/redis/connection/AbstractRedisConnection close ()V 177
[### PIXIE DUST ### org/springframework/data/redis/connection/jedis/JedisConnection close ()V 177
[### PIXIE DUST ### org/springframework/data/redis/core/RedisConnectionUtils releaseConnection (Lorg/springframework/data/
