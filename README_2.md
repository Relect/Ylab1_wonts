#  Проект привычки

   
   Для запуска приложения введите в терминале последовательно

   sudo docker-compose up <br>
   Дождитесь сборки и запуска контейнера,<br>
   откройте новый терминал этой дирректории и в ней запустите <br>
   mvn liquibase:update <br>
   mvn compile <br>
   mvn exec:java -Dexec.mainClass="website.ylab.Main"
   
stack: java 17, junit 5, mockito, lombok, docker-compose, postgreSQL, liquebase.
