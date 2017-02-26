package com.tibco.ma.common.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tibco.ma.common.Constants;

import de.odysseus.staxon.json.JsonXMLConfig;
import de.odysseus.staxon.json.JsonXMLConfigBuilder;
import de.odysseus.staxon.json.JsonXMLOutputFactory;

@Component
@Scope(value = "singleton")
public class JacksonJSONParser extends JSONParser {

	private static Logger logger = LoggerFactory
			.getLogger(JacksonJSONParser.class);

	@Override
	public JSON fetchXMLToJSON(String address) throws Exception {
		logger.debug("fetch xml to JSON address: {}", address);
		JSON json = fetchXMLToJSON(address, null);
		return json;
	}

	@Override
	public JSON fetchXMLToJSON(String address, String encoding)
			throws Exception {
		logger.debug("fetch xml to JSON address: {}, encoding: {}", address,
				encoding);
		URLConnection conn = null;
		InputStream in = null;
		URL url = null;
		try {
			url = new URL(address);
			conn = url.openConnection();
			conn.setReadTimeout(Constants.JSON_CONNECTION_READ_TIMEOUT);
			in = conn.getInputStream();

			String xml = IOUtils.toString(in, encoding);
			logger.debug("fetch xml content: {}", xml);
			ObjectMapper mapper = new ObjectMapper();
			// System.out.println(xml2json(xml));
			JsonNode actualObj = mapper.readTree(xml2json(xml));
			logger.debug("fetch jsonNode: {}", actualObj);
			return new JacksonJSON(actualObj);
		} catch (MalformedURLException e) {
			throw new Exception(e);
		} catch (IOException e) {
			throw new Exception(e);
		} catch (XMLStreamException e) {
			throw new Exception(e);
		} catch (FactoryConfigurationError e) {
			throw new Exception(e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				throw new Exception(e);
			}
		}
	}

	@Override
	public JSON fetchJSON(String address, String encoding) throws Exception {
		logger.debug("fetch JSON address: {}, encoding: {}", address, encoding);
		URLConnection conn = null;
		InputStream in = null;
		URL url = null;
		try {
			url = new URL(address);

			conn = url.openConnection();
			conn.setReadTimeout(Constants.JSON_CONNECTION_READ_TIMEOUT);
			in = conn.getInputStream();

			String json = IOUtils.toString(in, encoding);
			logger.debug("fetch json content: {}", json);

			return new JacksonJSON(json);
		} catch (MalformedURLException e) {
			throw new Exception(e);
		} catch (IOException e) {
			throw new Exception(e);
		} catch (FactoryConfigurationError e) {
			throw new Exception(e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				throw new Exception(e);
			}
		}
	}

	public JSON fetchJSONofGzip(String address) throws Exception {
		logger.debug("fetch JSON address: {}, encoding: {}", address);
		return fetchJSONofGzip(address, null);
	}

	public JSON fetchJSONofGzip(String address, String encoding)
			throws Exception {
		logger.debug("fetch JSON address: {}, encoding: {}", address, encoding);
		URLConnection conn = null;
		InputStream in = null;
		URL url = null;
		try {
			url = new URL(address);

			conn = url.openConnection();
			conn.setReadTimeout(Constants.JSON_CONNECTION_READ_TIMEOUT);
			in = conn.getInputStream();

			String json = IOUtils.toString(new GZIPInputStream(in), encoding);
			logger.debug("fetch json content: {}", json);

			return new JacksonJSON(json);
		} catch (MalformedURLException e) {
			throw new Exception(e);
		} catch (IOException e) {
			throw new Exception(e);
		} catch (FactoryConfigurationError e) {
			throw new Exception(e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				throw new Exception(e);
			}
		}
	}

	@Override
	public JSON buildJSON(String json) throws Exception {
		try {
			return new JacksonJSON(json);
		} catch (IOException e) {
			throw new Exception(e);
		}
	}

	private static String xml2json(String xml) throws IOException,
			XMLStreamException, FactoryConfigurationError {
		StringReader input = new StringReader(xml);
		StringWriter output = new StringWriter();
		JsonXMLConfig config = new JsonXMLConfigBuilder().autoArray(true)
				.autoPrimitive(true).prettyPrint(true).build();
		XMLEventReader reader = null;
		XMLEventWriter writer = null;
		try {
			reader = XMLInputFactory.newInstance().createXMLEventReader(input);
			writer = new JsonXMLOutputFactory(config)
					.createXMLEventWriter(output);
			writer.add(reader);

		} finally {
			reader.close();
			writer.close();
			output.close();
			input.close();
		}
		return output.toString();
	}

	@Override
	public JSON xmlToJson(String xml) throws Exception {
		try {
			return new JacksonJSON(xml2json(xml));
		} catch (IOException | XMLStreamException | FactoryConfigurationError e) {
			throw new Exception(e);
		}
	}

	@Override
	public JSON fetchJSON(String address) throws Exception {
		logger.debug("fetch JSON address: {}", address);
		return fetchJSON(address, null);
	}

}
