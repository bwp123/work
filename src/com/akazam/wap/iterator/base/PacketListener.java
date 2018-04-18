package com.akazam.wap.iterator.base;

import com.akazam.tools.rtsp.*;

public class PacketListener implements IPacketListener
{
	public static final int ERR_READY = -1;
	public static final int ERR_OK = 0;
	public static final int ERR_RTSP_CONN = 4;
	public static final int ERR_RTSP_INTR_AUD = 5;
	public static final int ERR_RTSP_INTR_VID = 6;
	public static final int ERR_RTSP_INTR_AV = 7;
	private long CONN_TIMEOUT = 30000L;
	private long RTST_TESTTIME = 5000L;
	private long RTSP_INTRTIME = 5000L;

	private long downSize = 0L;
	String result = "";
	Boolean available = null;

	public void doMtvTest(String url)
	{
		this.result = "";
		this.available = null;
		if (url != null)
		{
		    for(int i=0;i<3;i++)
		    {
				RtspClient client = new RtspClient(this);
				boolean succ = client.connect(url, (int) this.CONN_TIMEOUT, (int) this.RTSP_INTRTIME);
				if (!succ)
				{
					client.disconnect();
					continue;
				}
				succ = client.play();
				long ts = System.currentTimeMillis();
				if (!succ)
				{
					client.disconnect();
					continue;
				}
				try
				{
					Thread.sleep(this.RTST_TESTTIME);
				}
				catch (Exception e)
				{
				}
				ts = System.currentTimeMillis() - ts;
				this.downSize = (this.downSize * 1000L * 8L / ts);
				client.stop();
				client.disconnect();
				break;
		    }
		}
	}

	public void onError(int i, String string)
	{
		if (this.available == null)
		{
			this.available = Boolean.valueOf(false);
		}
	}

	public void onRecvData(int i, int tp, Object object)
	{
		this.downSize += i;
		if (this.available == null)
		{
			this.available = Boolean.valueOf(true);
		}
	}
	
	
	public static void main(String[] args)
	{
		System.out.println(testRtsp("rtsp://118.85.193.196/jiangsu_4/live-mp4-150k-zhejiang.smil.sdp?sign=BD4E581951E8B72FFCB8CC09A2106AA4&tm=527a081c&vw=3&ver=v1.2&d=live&playseek=20131105193600-20131105220000&msisdn=20000000000&spid=00000018&sid=11111111000000050000000000149217&nodeid=93068&pid=1000000033&channelid=019999&videotype=3&nettype=2&timestamp=20131106171300&H=0000&encrypt=3332b004c28bfcb179bb5d3f2ace9127&amp;mkId=0105000"));
	}
	
	
	public static Boolean testRtsp(String url)
	{
		PacketListener p=new PacketListener();
		p.doMtvTest(url);
	    return (p.getAvailable());
	}

	public Boolean getAvailable()
	{
		return available;
	}

}
