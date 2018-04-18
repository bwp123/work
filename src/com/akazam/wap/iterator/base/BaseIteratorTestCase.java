package com.akazam.wap.iterator.base;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.akazam.wap.client.base.BaseTestCase;
import com.akazam.wap.client.model.Link;
import com.akazam.wap.framework.Constant;
import com.akazam.wap.framework.core.utils.StringUtils;

public abstract class BaseIteratorTestCase
{

	private static final String JAVASCRIPT = "javascript:";

	private static final String HTTP = "http://";

	private static final String HTTPS = "https://";

	private static final String MAILTO = "mailto:";

	private static final String A_HREF = "a[href]";

	private static final String _IMG = "<img";

	private static final String IMG = "img";

	private static final String ALT = "alt";

	private static final String IMG_SRC = "img[src]";

	private static final String SPLIT_FH = ";";

	private static final String ABS = "abs:";

	private static final String SPLIT_EQ = "=";

	private static final String HTML = "html";

	private static final String UTF_8 = "utf-8";

	private static final String CHARSET = "charset";

	private static final String TITLE = "title";

	private static final String LEFT_GH = "(";

	private static final String RIGHT_GH = ")";

	private IteratorTestSuite suite = null;

	private Link link;

	public BaseIteratorTestCase(IteratorTestSuite suite)
	{
		this.suite = suite;
	}

	public abstract int doLogicTest();

	public IteratorTestSuite getSuite()
	{
		return suite;
	}

	public void setSuite(IteratorTestSuite suite)
	{
		this.suite = suite;
	}

	public Link getLink()
	{
		return link;
	}

	public void setLink(Link link)
	{
		this.link = link;
	}
	
	
	public static String doLogin()
	{
		String cookie = null;
		String result = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		ByteArrayOutputStream outStream = null;
		try
		{
			URL url = new URL("http://v.vnet.mobi/portal/480/tpl/10/8/userLoginAction.html");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(90000);
			conn.setReadTimeout(90000);
			conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			conn.setRequestProperty("User-Agent", "Java/1.6.0_33");
			conn.setRequestProperty("Accept", "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
			// conn.setRequestProperty("Connection", "close");
			OutputStream os = (OutputStream) conn.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
			osw.write("accountNo=18950104711&password=353603&sueUrl=/wap/home/wdfw/user/index.jsp&failUrl=/480/tpl/10/8/663.jsp&saveme=1&method:userLogin= 登 录");
			osw.flush();
			osw.close();
			int statusCode = conn.getResponseCode();
			if (statusCode != 200)
			{
				is = conn.getErrorStream();
			}
			else
			{
				is = conn.getInputStream();
			}
			
			outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[8192];
			int len = 0;
			while ((len = is.read(buffer)) != -1)
			{
				outStream.write(buffer, 0, len);
			}
			result = new String(outStream.toByteArray(), "utf-8");
			System.out.println(result);
			Map map = conn.getHeaderFields();
			if (map.get("Set-Cookie") != null) {
			cookie = (map.get("Set-Cookie")).toString();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
			result = e.getMessage();
		}
		finally
		{
			conn = null;
			try
			{
				if (is != null)
				{
					is.close();
				}
			}
			catch (Exception e)
			{
			}
			try
			{
				if (outStream != null)
				{
					outStream.close();
				}
			}
			catch (Exception e)
			{
			}
			outStream = null;
		}
		return cookie;
	}

	public void doTest(String cookie)
	{
		if (link != null)
		{
			int statusCode = BaseTestCase.BUSY;
			String url = link.getUrl();
			System.out.println("testing url "+url);
			String contentType = "";
			String charset = "";
			String name = "";
			boolean isIgnore=false;
			for(int i=0;i<suite.getBlackLinkList().size();i++)
			{
				if(link.getUrl()!=null&&link.getUrl().indexOf(suite.getBlackLinkList().get(i))>=0)
				{
					isIgnore=true;
					break;
				}
			}
			if (StringUtils.isEmpty(url))
			{
				System.out.println("empty url "+url);
				statusCode = BaseTestCase.EMPTY_URL;
				System.out.println(statusCode+",EMPTY_URL");
			}
			else if (url.toLowerCase().indexOf("javascript:") >= 0||isIgnore)
			{
				System.out.println("javascript url "+url);
				statusCode = BaseTestCase.IGNORE_TYPE;
				System.out.println(statusCode+",IGNORE_TYPE");
			}
			else if (url.toLowerCase().indexOf("mailto:") >= 0)
			{
				System.out.println("mailto url "+url);
				statusCode = BaseTestCase.MAIL_HOST_OK;
				System.out.println(statusCode+",MAIL_HOST_OK");
			}
			else if (url.toLowerCase().startsWith("file:"))
			{
				System.out.println("file url "+url);
				statusCode = BaseTestCase.NOT_FOUND;
				System.out.println(statusCode+",NOT_FOUND");
			}
			else if (url.toLowerCase().startsWith("rtsp://"))
			{
				System.out.println("rtsp url "+url);
				Boolean success=PacketListener.testRtsp(url);
				if(success!=null)
				{
					statusCode = success?BaseTestCase.OK:BaseTestCase.FAIL;
					System.out.println(statusCode+","+(success?"OK":"FAIL"));
				}
			}
			else
			{
				String baseUrl = BaseTestCase.getBaseUrl(url);
				String result = null;
				HttpURLConnection conn = null;
				InputStream instream = null;
				ByteArrayOutputStream outStream = null;
				long timeused = 0;
				// System.out.println("url:" + url);
				// System.out.println("getReferByUrl:" + link.getReferByUrl());
				for (int retry = 0; retry < BaseTestCase.MAX_CONN_RETRIES; retry++)
				{
//					try
//					{
//						Thread.sleep(1000);
//					}
//					catch (InterruptedException e1)
//					{
//					}
					try
					{
						long current = System.currentTimeMillis();
						boolean completed = true;
						int res = 0;
						URL u = new URL(url);
						conn = (HttpURLConnection) u.openConnection();
						conn.setConnectTimeout(90000);
						conn.setReadTimeout(90000);
						conn.setInstanceFollowRedirects(false);
						conn.setUseCaches(false);
						conn.setRequestProperty("User-Agent", this.suite.getUserAgent());
						conn.setRequestProperty("Connection", "keep-alive");
						if(!StringUtils.isEmpty(cookie))
						{
							conn.setRequestProperty("Cookie", cookie);
						}
						//conn.setRequestProperty("x-up-calling-line-id", Constant.MOBILEPHONE);// 在访问WAP门户活动栏目的链接时，如果不加此属性，会报403禁止访问错误
						conn.setRequestMethod(BaseTestCase.HTTP_REQUEST_GET);
						instream = conn.getInputStream();
						statusCode = conn.getResponseCode();
						if (statusCode == BaseTestCase.HTTP_CODE_302)
						{
							String redirecturl = conn.getHeaderField("Location");// 获取重定向的URL
							// 在测试过程发现，conn.getHeaderField("Location")的重定向URL有的是相对路径的情况
							if (redirecturl.toLowerCase().indexOf(HTTP) < 0 && redirecturl.toLowerCase().indexOf(HTTPS) < 0)
							{
								redirecturl = baseUrl + "/" + redirecturl;
							}
							url = redirecturl.replaceAll("\\s*", "");
							if (!StringUtils.isEmpty(url))
							{
								if (conn != null)
								{
									conn.disconnect();
									conn = null;
								}
								if (instream != null)
								{
									instream.close();
									instream = null;
								}
								baseUrl = BaseTestCase.getBaseUrl(url);
								u = new URL(url);
								conn = (HttpURLConnection) u.openConnection();
								conn.setConnectTimeout(90000);
								conn.setReadTimeout(90000);
								conn.setUseCaches(false);
								conn.setRequestProperty("User-Agent", this.suite.getUserAgent());
								conn.setRequestProperty("Connection", "keep-alive");
								//conn.setRequestProperty("x-up-calling-line-id", Constant.MOBILEPHONE);// 在访问WAP门户活动栏目的链接时，如果不加此属性，会报403禁止访问错误
								conn.setRequestMethod(BaseTestCase.HTTP_REQUEST_GET);
								instream = conn.getInputStream();
								statusCode = conn.getResponseCode();
							}
							else
							{
								statusCode = BaseTestCase.RESPONSE_NOT_200;
								System.out.println(statusCode+","+"RESPONSE_NOT_200");
							}
						}
						if (statusCode == BaseTestCase.HTTP_CODE_200)
						{
							statusCode = BaseTestCase.OK;
							System.out.println(statusCode+","+"OK");
						}
						else if (statusCode == BaseTestCase.HTTP_CODE_404)
						{
							statusCode = BaseTestCase.RESPONSE_404_ERROR;
							System.out.println(statusCode+","+"HTTP_CODE_404");
						}
						else
						{
							if (statusCode == -1)
							{
								statusCode = BaseTestCase.OK;
								System.out.println(statusCode+","+"OK");
								break;
							}
							statusCode = BaseTestCase.RESPONSE_NOT_200;
						}
						if (statusCode == BaseTestCase.OK)
						{
							contentType = (conn.getContentType());
							if (!StringUtils.isEmpty(contentType))
							{
								String[] types = contentType.split(SPLIT_FH);
								for (int i = 0; i < types.length; i++)
								{
									if (i == 0)
									{
										contentType = types[i];
									}
									else if (types[i].toLowerCase().indexOf(CHARSET) >= 0)
									{

										charset = types[i];
										String[] tmpTypes = types[i].split(SPLIT_EQ);
										if (tmpTypes.length > 1)
										{
											charset = tmpTypes[1];
										}
									}
								}
								this.link.setContentType(contentType);
								this.link.setCharset(charset);
							}
							if (contentType != null && contentType.toLowerCase().indexOf(HTML) >= 0)
							{
								outStream = new ByteArrayOutputStream();
								byte[] buffer = new byte[8192];
								int len = 0;
								while ((len = instream.read(buffer)) != -1)
								{
									outStream.write(buffer, 0, len);
								}
								timeused = (System.currentTimeMillis() - current);
								result = new String(outStream.toByteArray(), charset == null || charset.trim().length() == 0 ? UTF_8 : charset);
								//System.out.println("result:" + result);
								res = result.length();
								Document doc = Jsoup.parse(result, baseUrl);
								if (StringUtils.isEmpty(name))
								{
									name = doc.select(TITLE).text();
								}
								this.link.setName(name);
								// 如果是外链且WhiteBaseLinks不包含此外链，不再做进一步遍历,否则继续遍历
								if (this.suite.getWhiteBaseLinks().contains(baseUrl) && (this.suite.getMaxLevel() == -1 || this.link.getLevel() < this.suite.getMaxLevel()))
								{
									String[][] selectors = BaseTestCase.selectors;
									for (int i = 0; i < selectors.length; i++)
									{
										Elements links = doc.select(selectors[i][0]);
										for (Element link : links)
										{
											String urlStr = link.attr(selectors[i][1]);
											//System.out.println("---"+ link.attr("abs:"+ selectors[i][1]));
											if (urlStr != null)
											{
												if (urlStr.trim().length() == 0 || urlStr.toLowerCase().indexOf(JAVASCRIPT) >= 0 || (urlStr.indexOf(LEFT_GH) >= 0 && urlStr.indexOf(RIGHT_GH) >= 0) || urlStr.toLowerCase().indexOf(MAILTO) >= 0)
												{
													urlStr = urlStr.trim();
												}
												else if (!urlStr.startsWith(HTTP)
														&& !urlStr.startsWith(HTTPS))
												{
													urlStr = link.attr(ABS + selectors[i][1]);
												}
												String nameStr = "";
												if (selectors[i][0].equals(A_HREF))
												{
													nameStr = link.html();
													if (nameStr.toLowerCase().indexOf(_IMG) >= 0)
													{
														try
														{
															Document d = Jsoup.parse(nameStr);
															nameStr = d.select(IMG).get(0).attr(ALT);
														}
														catch (Exception e)
														{
															nameStr = "";
														}
													}
													else
													{
														nameStr = link.text();
													}
												}
												else if (selectors[i][0].equals(IMG_SRC))
												{
													nameStr = link.attr(ALT);
												}
												Link nl = this.link.newLink(urlStr.replaceAll("\\s*", ""), nameStr, this.link.getLevel() + 1);
												Link l = this.suite.addLink(nl);
												l.addReferencedByLinks(this.link);
												this.link.addSubLinks(l);
											}
										}
									}
								}
							}
							else
							{
								byte[] buf = new byte[8192];
								int len = instream.read(buf, 0, buf.length);
								while (len != -1)
								{
									res += len;
									len = instream.read(buf, 0, buf.length);
									if (res >= 10000)
									{
										completed = false;
										break;
									}
								}
								timeused = (System.currentTimeMillis() - current);
							}
							if (completed)
							{
								long size = (long) res;
								Double duration = timeused / 1000d;
								this.link.setSize(size);
								this.link.setDuration(duration);
							}
							Double speed = (res / timeused * 1d) / 1024;
							if (speed != null && speed != 0.0)
							{
								BigDecimal bd = new BigDecimal(speed);
								bd = bd.setScale(3, BigDecimal.ROUND_HALF_EVEN);
								speed = bd.doubleValue();
								this.link.setSpeed(speed);
							}
						}
					}
					catch (MalformedURLException e)
					{
						e.printStackTrace(System.out);
						statusCode = BaseTestCase.REQUEST_URL_ERROR;
						System.out.println(statusCode+","+"REQUEST_URL_ERROR");
					}
					catch (java.net.ConnectException e)
					{
						e.printStackTrace(System.out);
						String msg = e.getMessage();
						if (msg.indexOf("Connection timed out") >= 0)
						{
							statusCode = BaseTestCase.REQUEST_TIME_OUT;
						}
						else
						{
							statusCode = BaseTestCase.REQUEST_CONNECT_ERROR;
						}
						System.out.println(statusCode+","+(msg.indexOf("Connection timed out") >= 0?"REQUEST_TIME_OUT":"REQUEST_CONNECT_ERROR"));
					}
					catch (java.net.SocketTimeoutException e)
					{
						e.printStackTrace(System.out);
						String msg = e.getMessage();
						if (msg.indexOf("Connection timed out") >= 0)
						{
							statusCode = BaseTestCase.REQUEST_TIME_OUT;
						}
						else
						{
							statusCode = BaseTestCase.RESPONSE_TIME_OUT;
						}
						System.out.println(statusCode+","+(msg.indexOf("Connection timed out") >= 0?"REQUEST_TIME_OUT":"RESPONSE_TIME_OUT"));

					}
					catch (java.io.FileNotFoundException e)
					{
						e.printStackTrace(System.out);
						statusCode = BaseTestCase.RESPONSE_404_ERROR;
						System.out.println(statusCode+","+"RESPONSE_404_ERROR");
					}
					catch (java.net.SocketException e)
					{
						e.printStackTrace(System.out);
						statusCode = BaseTestCase.NET_ERROR;
						System.out.println(statusCode+","+"NET_ERROR");
					}
					catch (java.io.IOException e)
					{
						e.printStackTrace(System.out);
						statusCode = BaseTestCase.RESPONSE_NOT_200;
						System.out.println(statusCode+","+"RESPONSE_NOT_200");
					}
					catch (Exception e)
					{
						e.printStackTrace(System.out);
						statusCode = BaseTestCase.RESPONSE_NOT_200;
						System.out.println(statusCode+","+"RESPONSE_NOT_200");
					}
					finally
					{
						if (conn != null)
						{
							conn.disconnect();
						}
						conn = null;
						try
						{
							if (instream != null)
							{
								instream.close();
							}
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						try
						{
							if (outStream != null)
							{
								outStream.close();
							}
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						instream = null;
						outStream = null;
					}
					if (statusCode == BaseTestCase.OK)
					{
						break;
					}
				}
			}
			this.suite.addCompleted();
			if (statusCode != BaseTestCase.OK && statusCode != BaseTestCase.IGNORE_TYPE && statusCode != BaseTestCase.MAIL_HOST_OK && statusCode != BaseTestCase.BUSY)
			{
				this.suite.foundError();
				this.link.setResult(BaseTestCase.FAIL_STR);
			}
			else
			{
				this.link.setResult(BaseTestCase.SUCCESS_STR);
			}
			this.link.setDescription(Constant.getCodeDescription(statusCode));
			this.suite.showProgress();
		}
	}
	
	
	public static void main(String[] args)
	{
		System.out.println(doLogin());
	}

}