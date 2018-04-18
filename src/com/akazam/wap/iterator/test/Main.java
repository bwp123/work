package com.akazam.wap.iterator.test;


public class Main
{
	
	public static String getUrl(int categoryId,int fid,String path,String token,boolean isLive)
	{
		String url="";
		if(Main3.categoryId==categoryId)
		{
			if(fid==0)
			{
				//collect
				if(!isLive)
				{
					String[] urls=Main3.getVodDetailUrls("", "",  path,  token,false);
					if(urls!=null)
					{
						url=urls[3];
					}
				}
			}
			else if(fid==1)
			{
				//download
				if(!isLive)
				{
					String[] urls=Main3.getVodDetailUrls("", "",  path,  token,false);
					if(urls!=null)
					{
						url=urls[4];
					}
				}
			}
			else if(fid==2)
			{
				//play
				if(!isLive)
				{
					String[] urls=Main3.getVodDetailUrls("", "",  path,  token,false);
					if(urls!=null)
					{
						url=urls[5];
					}
				}
				else
				{
					String[] urls=Main3.getLiveUrl(path, token);
					if(urls!=null&&urls[0]!=null)
					{
						url=urls[0];
					}
				}
			}
		}
		else if(Main4.categoryId==categoryId)
		{
			if(fid==0)
			{
				if(!isLive)
				{
					url=Main4.genCollectionUrl(path, token);
				}
			}
			else if(fid==1)
			{
				if(!isLive)
				{
					url=Main4.getDownloadUrl(path, token);
				}
			}
			else if(fid==2)
			{
				url=Main4.getPlayUrl(isLive?"":path, isLive?path:"", token);
			}
		}
		else if(Main5.categoryId==categoryId)
		{
			if(fid==10000)
			{
				if(!isLive)
				{
					try
					{
						String[] ps=path.split(",");
						url=Main5.getLiveVodPlayUrl(ps[0], ps[1], token);
					}
					catch(Exception e)
					{
					}
				}
			}
			else if(fid==0)
			{
				if(!isLive)
				{
					url=Main5.genCollectionUrl(path, token);
				}
			}
			else if(fid==1)
			{
				if(!isLive)
				{
					url=Main5.getDownloadUrl(path, token);
				}
			}
			else if(fid==2)
			{
				url=Main5.getPlayUrl(isLive?"":path, isLive?path:"", token);
			}
		}
		else if(MainWap.categoryId==categoryId)
		{
			if(fid==10000)
			{
				MainWap.token=token;
				String[] urls=MainWap.getLiveVod(url, "", "");
				if(urls!=null)
				{
					url=urls[5];
				}
			}
			else
			{
				MainWap.token=token;
				String[] urls=MainWap.getProgramDetailUrls("", path);
				if(fid==0)
				{
					if(urls!=null)url=urls[3];
				}
				else if(fid==1)
				{
					if(urls!=null)url=urls[4];
				}
				else if(fid==2)
				{
					if(urls!=null)url=urls[5];
				}
			}
		}
		return url;
	}
	
	public static void main(String[] args)
	{
		/*
		String token = MainWap.getToken("http://v.vnet.mobi/portal/480/tpl/10/8/userLoginAction.html", true);
		String liveUrl=getUrl(MainWap.categoryId,2,"http://v.vnet.mobi//portal/480//live/11111111000000050000000000101800/index.jsp", token,true);
		System.out.println(liveUrl);
		String collectUrl=getUrl(MainWap.categoryId,0,"http://v.vnet.mobi//portal/480//video/yl/ylbg/n70989d169c20366626.jsp?mkId=AQNK1051508", token,false);
		System.out.println(collectUrl);
		String downloadUrl=getUrl(MainWap.categoryId,1,"http://v.vnet.mobi//portal/480//video/yl/ylbg/n70989d169c20366626.jsp?mkId=AQNK1051508", token,false);
		System.out.println(downloadUrl);
		String playUrl=getUrl(MainWap.categoryId,2,"http://v.vnet.mobi//portal/480//video/yl/ylbg/n70989d169c20366626.jsp?mkId=AQNK1051508", token,false);
		System.out.println(playUrl);
		*/
		/*
		String token = Main3.getToken();
		Main3.doLogin(token);
		String liveUrl=getUrl(Main3.categoryId,2,"http://v.vnet.mobi//portal/480//live/11111111000000050000000000101800/index.jsp", token,true);
		System.out.println(liveUrl);
		String collectUrl=getUrl(Main3.categoryId,0,"http://v.vnet.mobi//portal/480//video/yl/ylbg/n70989d169c20366626.jsp?mkId=AQNK1051508", token,false);
		System.out.println(collectUrl);
		String downloadUrl=getUrl(Main3.categoryId,1,"http://v.vnet.mobi//portal/480//video/yl/ylbg/n70989d169c20366626.jsp?mkId=AQNK1051508", token,false);
		System.out.println(downloadUrl);
		String playUrl=getUrl(Main3.categoryId,2,"http://v.vnet.mobi//portal/480//video/yl/ylbg/n70989d169c20366626.jsp?mkId=AQNK1051508", token,false);
		System.out.println(playUrl);
		*/
		/*
		String token = Main4.getToken();
		Main4.doHttpGet(Main4.genLoginUrl(token));
		String liveUrl=getUrl(Main4.categoryId,2,"11111111000000050000000000101941", token,true);
		System.out.println(liveUrl);
		String collectUrl=getUrl(Main4.categoryId,0,"31986424", token,false);
		System.out.println(collectUrl);
		String downloadUrl=getUrl(Main4.categoryId,1,"31986424", token,false);
		System.out.println(downloadUrl);
		String playUrl=getUrl(Main4.categoryId,2,"31986424", token,false);
		System.out.println(playUrl);
		*/
		/*
		String token = Main5.getToken();
		Main5.doHttpGet(Main5.genLoginUrl(token));
		String liveUrl=getUrl(Main5.categoryId,2,"11111111000000050000000000101941", token,true);
		System.out.println(liveUrl);
		String collectUrl=getUrl(Main5.categoryId,0,"31986424", token,false);
		System.out.println(collectUrl);
		String downloadUrl=getUrl(Main5.categoryId,1,"31986424", token,false);
		System.out.println(downloadUrl);
		String playUrl=getUrl(Main5.categoryId,2,"31986424", token,false);
		System.out.println(playUrl);
		String vodPlayUrl=getUrl(Main5.categoryId,10000,"934368,11111111000000050000000000101802", token,false);
		System.out.println(vodPlayUrl);
		*/
	}
}
