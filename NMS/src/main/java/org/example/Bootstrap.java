package org.example;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.example.utils.Constant.PROVISION_DEVICES_JSON_FILE_PATH;

public class Bootstrap
{
    public static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

    public static JsonArray PROVISION_DEVICES_LIST = new JsonArray();

    public static void main(String[] args)
    {
        var vertx = Vertx.vertx();

        vertx.fileSystem().readFile(PROVISION_DEVICES_JSON_FILE_PATH, readResult -> {
            if(readResult.succeeded())
            {
                var buffer = readResult.result();

                PROVISION_DEVICES_LIST = buffer.toJsonArray();

                LOGGER.info("Provision devices file loaded successfully! Devices available: {}", PROVISION_DEVICES_LIST.size());
            }
            else
            {
                LOGGER.error("Cannot read provision devices file.");
            }
        });

        vertx.deployVerticle(Server.class.getName(), handler -> {
            if(handler.succeeded())
            {
                LOGGER.info("Server verticle deployed");
            }
        });
    }
}