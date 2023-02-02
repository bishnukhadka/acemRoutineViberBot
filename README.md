# acemRoutineViberBot
ACEM Routine Viber Bot
This is a simple viber bot that provides routine for the class schedule of ACEM, sunday to friday. This application uses the Viber Rest API to send and recieve messages. 

Necessary environment variables needed are as follows:
```
spring_profiles_active=<YOUR-ACTIVE-PROFILE>
PGHOST=<HOST-NAME>
PGPORT=<YOUR-PORT-HERE>
PGDATABASE=<YOUR-DATABASE-NAME>
PGPASSWORD=<YOUR-PASSWORD-HERE>
PGUSER=<USERNAME>
VIBER_AUTH_TOKEN=<AUTH-TOKEN>
VIBER_WEBHOOK_URL=<WEBHOOK-URL>

```

Viber Auth Token can be recieved by creating a viber bot. And Webhook url should have a valid SSL certificate, for security purposes.
