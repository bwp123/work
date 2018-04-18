package com.akazam.wap.iterator.common;

import java.util.*;

import com.akazam.common.dbaccess.DBAccess;
import com.akazam.wap.iterator.test.Main3;
import com.akazam.wap.iterator.test.Main4;
import com.akazam.wap.iterator.test.Main5;
import com.akazam.wap.iterator.test.MainWap;

public class DBHelper
{

	public static void saveErrorLog(String mobile,String error,String testData,String url,String testModel,int categoryid,String category)
	{
		
		DBAccess dbaccess = new DBAccess();
		try
		{
			dbaccess.open();
			String sql="insert into test_log set ts="+System.currentTimeMillis()+", os='后台',terminal='平台',imsi='',appver='',agentid=10000,mobile='"+mobile+"', error='"+error+"', testData='"+testData+"', url='"+url+"', testModel='"+testModel+"', categoryid="+categoryid+", category='"+category+"'";
			dbaccess.update(sql);
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}
		finally
		{
			if (dbaccess != null) dbaccess.close();
		}
	}
	
	public static Map<String, String> saveChannels(int categoryId, int parentId, List<String> channels, boolean isLive)
	{
		Map<String, String> channelMap = new HashMap<String, String>();
		List<String> handled = new ArrayList<String>();
		String sql = "select id,name from new_channel c where parent=" + parentId;
		DBAccess dbaccess = new DBAccess();
		try
		{
			dbaccess.open();
			dbaccess.query(sql);
			if (dbaccess.getResultSet() != null)
			{
				while (dbaccess.getResultSet().next())
				{
					channelMap.put(dbaccess.getResultSet().getString("name"), dbaccess.getResultSet().getString("id"));
				}
			}
			// insert or update
			for (int i = 0; i < channels.size(); i++)
			{
				String name = channels.get(i);
				String[] strs = name.split(",");
				String[] names = strs[0].split("=");
				String[] paths = strs[1].split("=");
				if(names.length==1)
				{
					names=new String[]{"",names[0]};
				}
				if(paths.length==1)
				{
					paths=new String[]{"",paths[0]};
				}
				String[] cnames = names[1].split("->");
				String cname = categoryId==Main5.categoryId?cnames[2]:cnames[1];
				if (channelMap.containsKey(cname))
				{
					if (!handled.contains(cname))
					{
						handled.add(cname);
						if (isLive)
						{
							dbaccess.update("update new_channel set name='" + cname + "',parent=" + parentId + ",productPackageId='" + paths[1] + "',level=3,islive=1,categoryid=" + categoryId + " where id=" + channelMap.get(cname));
						}
						else
						{
							dbaccess.update("update new_channel set name='" + cname + "',parent=" + parentId + ",url='" + paths[1] + "',level=3,islive=0,categoryid=" + categoryId + " where id=" + channelMap.get(cname));
						}
					}
				}
				else
				{
					if (!handled.contains(cname))
					{
						handled.add(cname);
						if (isLive)
						{
							dbaccess.update("insert into new_channel set name='" + cname + "',parent=" + parentId + ",productPackageId='" + paths[1] + "',level=3,islive=1,categoryid=" + categoryId);
						}
						else
						{
							dbaccess.update("insert into new_channel set name='" + cname + "',parent=" + parentId + ",url='" + paths[1] + "',level=3,islive=0,categoryid=" + categoryId);
						}
					}
				}
			}
			// reload
			sql = "select id,name from new_channel c where parent=" + parentId;
			channelMap.clear();
			dbaccess.query(sql);
			if (dbaccess.getResultSet() != null)
			{
				while (dbaccess.getResultSet().next())
				{
					channelMap.put(dbaccess.getResultSet().getString("name"), dbaccess.getResultSet().getString("id"));
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);

		}
		finally
		{
			if (dbaccess != null) dbaccess.close();
		}
		return channelMap;
	}

	public static void saveVideos(int categoryId, int pchannelid, Map<String, String> channelMap, List<String[]> records, boolean isLive)
	{
		long ts = System.currentTimeMillis();
		DBAccess dbaccess = new DBAccess();
		List<String> channelIds=new ArrayList<String>();
		try
		{
			dbaccess.open();
			for (int i = 0; i < records.size(); i++)
			{
				String[] strs = records.get(i);
				String name = strs[0];
				String fullname = strs[1];
				//String id = strs[2];
				String collectUrl = strs[3];
				String downloadUrl = strs[4];
				String playUrl = strs[5];
				String path= strs[6];
				String[] cnames = fullname.split("->");
				String channelId = channelMap.get(Main5.categoryId==categoryId?cnames[2]:cnames[1]);
				if (channelId != null && channelId.length() > 0)
				{
					int live=(downloadUrl==null||downloadUrl.trim().length()==0)?1:0;
					if(!channelIds.contains(channelId))
					channelIds.add(channelId);
					List<String[]> urls = new ArrayList<String[]>();
					if (collectUrl != null && collectUrl.length() > 0)
					{
						urls.add(new String[]{collectUrl,"0"});
					}
					if (downloadUrl != null && downloadUrl.length() > 0)
					{
						urls.add(new String[]{downloadUrl,"1"});
					}
					if (playUrl != null && playUrl.length() > 0)
					{
						urls.add(new String[]{playUrl,"2"});
					}
					for (int j = 0; j < urls.size(); j++)
					{
				        
						String channel=Main5.categoryId==categoryId?cnames[2]:cnames[1];
						String pchannel=cnames[0];
						if(Main5.categoryId==categoryId)
						{
							if(cnames.length>4)
							{
								channel=cnames[3];
								pchannel=cnames[2];
							}
						}
						else if(Main4.categoryId==categoryId)
						{
							if(cnames.length>3)
							{
								channel=cnames[2];
								pchannel=cnames[1];
							}
						}
						else if(Main3.categoryId==categoryId)
						{
							if(cnames.length>3)
							{
								channel=cnames[2];
								pchannel=cnames[1];
							}
						}
						else if(MainWap.categoryId==categoryId)
						{
							if(cnames.length>3)
							{
								channel=cnames[2];
								pchannel=cnames[1];
							}
						}
						dbaccess.update("insert into new_channel_video set title='" + name + "',url='"+urls.get(j)[0]+"',ts="+ts+",channelid="+channelId+",pchannleid="+pchannelid+",categoryid="+categoryId+",fid="+urls.get(j)[1]+",channel='"+channel+"',pchannel='"+pchannel+"',expired=0,live="+live+",path='"+path+"'");
						if(j==0&&strs.length>=9)
						{
							dbaccess.update("insert into new_channel_video set title='" + name + "',url='"+strs[7]+"',ts="+ts+",channelid="+channelId+",pchannleid="+pchannelid+",categoryid="+categoryId+",fid=10000,channel='"+channel+"',pchannel='"+pchannel+"',expired=0,live="+0+",path='"+strs[8]+"'");
						}
					}
				}
			}
			//expire
		    if(channelIds.size()>0)
			dbaccess.update("update new_channel_video set expired=1 where channelid in ("+ array2String(channelIds, ",")+") and ts<"+ts);
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);

		}
		finally
		{
			if (dbaccess != null) dbaccess.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static String array2String(List str, String seperator)
	{
		String result = "";
		if (str == null || str.size() == 0)
		{
			return result;
		}
		boolean isFirst = true;
		for (int i = 0; i < str.size(); i++)
		{
			if (isFirst)
			{
				isFirst = false;
			}
			else
			{
				result += seperator == null ? "" : seperator;
			}
			result += str.get(i) == null ? "" : str.get(i);

		}
		return result;
	}

	public static void main(String[] args)
	{

	}
}
