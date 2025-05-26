package com.okestro.resource.config.web.gzip;

import com.okestro.resource.config.web.exception.IllegalCompressionRequestException;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class GzipServerHttpRequest extends HttpServletRequestWrapper {
	public GzipServerHttpRequest(HttpServletRequest request) {
		super(request);
	}

	@Override
	public ServletInputStream getInputStream() {
		try {
			return new GZIPInputStreamAdapter(new GZIPInputStream(getRequest().getInputStream()));
		} catch (Exception e) {
			throw new IllegalCompressionRequestException(e.getMessage());
		}
	}

	private static class GZIPInputStreamAdapter extends ServletInputStream {
		private final GZIPInputStream gzipInputStream;
		private boolean eof = false;

		public GZIPInputStreamAdapter(GZIPInputStream gzipInputStream) {
			this.gzipInputStream = gzipInputStream;
		}

		@Override
		public boolean isFinished() {
			return eof;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener readListener) {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		@Override
		public int read() throws IOException {
			int result = gzipInputStream.read();
			if (result == -1) {
				eof = true;
			}
			return result;
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			int result = gzipInputStream.read(b, off, len);
			if (result == -1) {
				eof = true;
			}
			return result;
		}
	}
}
