package com.okestro.resource.config.web.gzip;

import static com.okestro.resource.config.web.gzip.GzipCompressionUtils.GZIP;

import com.okestro.resource.config.web.CompressionHttpResponse;
import com.okestro.resource.config.web.properties.CompressionUrlProperties;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

public class GzipServerHttpResponse extends CompressionHttpResponse {

	public GzipServerHttpResponse(HttpServletResponse delegate, CompressionUrlProperties properties) {
		super(delegate, properties);
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (getStatus() >= 400) {
			return super.getOutputStream();
		}

		if (!isMimeTypeMatches()) {
			return super.getOutputStream();
		}

		if (!isResponseSizeValid((long) super.getBufferSize())) {
			return super.getOutputStream();
		}

		setCompressionHeaders(GZIP);
		return new GzipOutputStreamAdapter(super.getOutputStream());
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return new PrintWriter(new OutputStreamWriter(getOutputStream(), getCharacterEncoding()));
	}

	static class GzipOutputStreamAdapter extends ServletOutputStream {
		private final GZIPOutputStream gzipOutputStream;
		private final ServletOutputStream originalStream;

		public GzipOutputStreamAdapter(final ServletOutputStream originalStream) throws IOException {
			this.originalStream = originalStream;
			this.gzipOutputStream = new GZIPOutputStream(originalStream, true);
		}

		@Override
		public boolean isReady() {
			return originalStream.isReady();
		}

		@Override
		public void setWriteListener(final WriteListener writeListener) {
			originalStream.setWriteListener(writeListener);
		}

		@Override
		public void write(final int b) throws IOException {
			this.gzipOutputStream.write(b);
		}

		@Override
		public void flush() throws IOException {
			this.gzipOutputStream.flush();
			this.originalStream.flush();
		}

		@Override
		public void close() throws IOException {
			try {
				this.gzipOutputStream.finish();
			} finally {
				this.originalStream.close();
			}
		}
	}
}
