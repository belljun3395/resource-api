package com.okestro.resource.support.web.handler;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingHandler {

	public void writeLog(Exception ex, HttpServletRequest request) {
		try {
			log.error(
					LOG_MESSAGE_FORMAT, request.getMethod(), request.getRequestURL(), ex.getMessage(), ex);
		} catch (Exception e) {
			log.error(LOG_MESSAGE_FORMAT, UNCAUGHT_LOG_MESSAGE, UNCAUGHT_LOG_MESSAGE, e.getMessage(), e);
		}
	}

	private static final String LOG_MESSAGE_FORMAT = "{} '{}' - {}";
	private static final String UNCAUGHT_LOG_MESSAGE = "??";

	private static final Set<String> IGNORE_REQUEST_URI_LIST;

	static {
		Set<String> uris = new HashSet<>();
		Collections.addAll(
				uris,
				"/",
				"/.aws/credentials",
				"/.env",
				"/.env.",
				"/.env.backup",
				"/.env.bak",
				"/.env.dev",
				"/.env.dev.local",
				"/.env.development.local",
				"/.env.ec2-3-34-67-210",
				"/.env.ec2-3-37-143-124",
				"/.env.example",
				"/.env.live",
				"/.env.local",
				"/.env.old",
				"/.env.prod",
				"/.env.prod.local",
				"/.env.production",
				"/.env.production.local",
				"/.env.save",
				"/.env.stage",
				"/.env.www",
				"/.env_1",
				"/.env_sample",
				"/.git/HEAD",
				"/.git/config",
				"/.git2/config",
				"/.svn/wc.db",
				"/.well-known/security.txt",
				"/1-200611214053U8.jpg",
				"/202110/images/public.css",
				"/6/",
				"/71624567",
				"/99vt",
				"/99vu",
				"/9h/",
				"/:80:undefined?id=",
				"/?XDEBUG_SESSION_START=phpstorm",
				"/AWSconf.git/config",
				"/Application/Buy/Static/js/if.js",
				// 생략된 URI 목록 ...
				"/ztp/cgi-bin/handle",
				"/zz/address.php?gid=651",
				"/zz2/address.php?gid=651");
		IGNORE_REQUEST_URI_LIST = Collections.unmodifiableSet(uris);
	}

	public static Set<String> getIgnoreRequestUriList() {
		return IGNORE_REQUEST_URI_LIST;
	}
}
