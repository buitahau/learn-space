Run elasticsearch

``docker run -d --name es7173 -p 9200:9200 -e "discovery.type=single-node" elasticsearch:7.17.3``

Document

https://markheath.net/post/exploring-elasticsearch-with-docker

Run elastic search docker compose

``docker-compose -f docker-compose-elasticsearch.yml up``

Run elastic search with kibana and head plugin docker compose

``docker-compose -f docker-compose-kibana-headplugin.xml up -d``

Error

max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]

=> run: ``sudo sysctl -w vm.max_map_count=262144``

---
Run keycloak

``docker-compose -f docker-compose-keycloak.yml up``

Keycloak API

https://developers.redhat.com/blog/2020/11/24/authentication-and-authorization-using-the-keycloak-rest-api#keycloak_connection_using_a_java_application

---

Run zipkin server

``docker-compose -f docker-compose-zipkin.yml up``

---

Jib tutorial

``mvn clean install jib:dockerBuild -Dapp.image.tag={tag}``

https://betulsahinn.medium.com/dockerizing-a-spring-boot-application-and-using-the-jib-maven-plugin-95c329866f34

https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin
