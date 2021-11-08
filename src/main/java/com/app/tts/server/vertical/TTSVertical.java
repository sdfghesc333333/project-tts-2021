/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.tts.server.vertical;

import com.app.tts.server.handler.base.ListBaseHandler;
import com.app.tts.server.handler.option.OptionHandler;
import com.app.tts.server.handler.option.OrderNotifyHandler;
import com.app.tts.server.handler.common.ExceptionHandler;
import com.app.tts.server.handler.common.RequestLoggingHandler;
import com.app.tts.server.handler.common.ResponseHandler;
import com.app.tts.util.LoggerInterface;
import com.app.tts.util.StringPool;

import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpClient;
import io.vertx.rxjava.core.http.HttpServer;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.handler.BodyHandler;
import io.vertx.rxjava.ext.web.handler.CookieHandler;
import io.vertx.rxjava.ext.web.handler.ResponseTimeHandler;
import io.vertx.rxjava.ext.web.handler.SessionHandler;
import io.vertx.rxjava.ext.web.handler.TimeoutHandler;
import io.vertx.rxjava.ext.web.sstore.LocalSessionStore;

/**
 *
 * @author hungdt
 */
public class TTSVertical extends AbstractVerticle implements LoggerInterface {

	private String serverHost;
	private int serverPort;
	private boolean connectionKeepAlive;
	private long connectionTimeOut;
	private int connectionIdleTimeOut;
	private String apiPrefix;

	public static HttpClient httpClient;
	public static HttpClient httpsClient;

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public void setConnectionKeepAlive(boolean connectionKeepAlive) {
		this.connectionKeepAlive = connectionKeepAlive;
	}

	public void setConnectionTimeOut(long connectionTimeOut) {
		this.connectionTimeOut = connectionTimeOut;
	}

	public void setConnectionIdleTimeOut(int connectionIdleTimeOut) {
		this.connectionIdleTimeOut = connectionIdleTimeOut;
	}

	public void setApiPrefix(String apiPrefix) {
		this.apiPrefix = apiPrefix;
	}

	@Override
	public void start() throws Exception {

		logger.info("[INIT] STARTING UP ORDER API SERVER...");

		httpClient = vertx.createHttpClient();
		httpsClient = vertx.createHttpClient(new HttpClientOptions().setSsl(true).setTrustAll(true));

		super.start();

		Router router = Router.router(vertx);
		router.route().handler(CookieHandler.create());
		router.route().handler(BodyHandler.create());
		router.route().handler(io.vertx.rxjava.ext.web.handler.CorsHandler.create("*")
				.allowedMethod(io.vertx.core.http.HttpMethod.GET)
				.allowedMethod(io.vertx.core.http.HttpMethod.POST)
				.allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
				.allowedHeader("Access-Control-Request-Method")
				.allowedHeader("Access-Control-Allow-Credentials")
				.allowedHeader("Access-Control-Allow-Origin")
				.allowedHeader("Access-Control-Allow-Headers")
				.allowedHeader("Content-Type"));

		router.route().handler(ResponseTimeHandler.create());
		router.route().handler(TimeoutHandler.create(connectionTimeOut));
		router.route().handler(new RequestLoggingHandler());

		router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx, "test tts", 30000))
				.setCookieHttpOnlyFlag(true).setCookieSecureFlag(true));

		router.mountSubRouter(apiPrefix, initAPI());

		router.route().failureHandler(new ExceptionHandler());
		router.route().last().handler(new ResponseHandler());

		HttpServerOptions httpServerOptions = new HttpServerOptions();

		httpServerOptions.setHost(serverHost);
		httpServerOptions.setPort(serverPort);
		httpServerOptions.setTcpKeepAlive(connectionKeepAlive);
		httpServerOptions.setIdleTimeout(connectionIdleTimeOut);

		HttpServer httpServer = vertx.createHttpServer(httpServerOptions);

		httpServer.requestHandler(router);

		httpServer.listen(result -> {
			if (result.failed()) {
				logger.error("[INIT] START TTS API ERROR " + result.cause());
			} else {
				logger.info("[INIT] TTS SERVER STARTED AT " + StringPool.SPACE + serverHost + StringPool.COLON + serverPort);
			}
		});
	}

	private Router initAPI() {

		Router router = Router.router(vertx);
		
		// xet uri de xem handler nao se bat login, handler nao khong bat login
		router.route(HttpMethod.POST, "/notifyOrder/:source").handler(new OrderNotifyHandler());
		router.route(HttpMethod.OPTIONS, "/login").handler(new OptionHandler());

		//api
		router.route(HttpMethod.GET, "/list-base").handler(new ListBaseHandler());

		return router;
	}
}
