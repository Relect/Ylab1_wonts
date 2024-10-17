#  Проект привычки

   
   Для запуска приложения введите в терминале последовательно

   sudo docker-compose up<br>
   Откройте новый терминал этой дирректории и в ней запустите<br>
   mvn liquibase:update
   mvn compile <br>
   mvn exec:java -Dexec.mainClass="website.ylab.Main"
   
stack: java 17, junit 5, mockito, lombok, docker-compose, postgreSQL, liquebase.
