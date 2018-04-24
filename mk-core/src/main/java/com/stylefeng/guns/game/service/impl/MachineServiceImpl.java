package com.stylefeng.guns.game.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import com.stylefeng.guns.core.redis.RedisService;
import com.stylefeng.guns.core.util.HttpClientUtil;
import com.stylefeng.guns.game.GameBaseProperties;
import com.stylefeng.guns.game.GameProcessEnum;
import com.stylefeng.guns.game.GameProcessUtil;
import com.stylefeng.guns.game.service.MachineService;

public class MachineServiceImpl implements MachineService {
	//public static PropFileManager propFileMgr = new PropFileManager("interface.properties");
	private GameBaseProperties gameProperties;
	private RedisService redisUtil;
	 //private MachineService machineService = MachineServiceImpl.getInstance();
	//游戏状态控制
	GameProcessUtil process = null;
	//初始化  游戏核心
	public void init() {
		process = GameProcessUtil.getInstance(redisUtil);
	}
	
	public String  validateToken(String token,String userId) throws ClientProtocolException{
		 List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("memberId", userId));
       // String validateTokenUrl = propFileMgr.getProperty("webapi.validateToken");
        String validateTokenUrl = gameProperties.getValidateToken();
        String httpResponse = HttpClientUtil.getInstance().executeByPOST(validateTokenUrl, params);
        return httpResponse;
	}
	
	@Override
	public void consumeGame(String token,Integer userId,Integer dollId) throws ClientProtocolException {
		if (leaveConsume(userId, dollId)) {//是否补发扣费
			//String consumeUrl = propFileMgr.getProperty("webapi.consumeGame");
			String consumeUrl = gameProperties.getConsumeGame();
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("memberId",String.valueOf(userId)));
			params.add(new BasicNameValuePair("dollId",String.valueOf(dollId)));
			params.add(new BasicNameValuePair("token",token));
			params.add(new BasicNameValuePair("state","1"));
			HttpClientUtil.getInstance().executeByPOST(consumeUrl, params);
			//logger.info("未正常结束补扣费");
		}
	}
	
	@Override
	public void historyGame(String token, Integer userId, Integer dollId) throws ClientProtocolException {
		if (this.leaveHistory(userId, dollId)) {
		//String endRoundUrl = propFileMgr.getProperty("webapi.endRound");
		String endRoundUrl = gameProperties.getEndRound();
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("memberId",String.valueOf(userId)));
		params.add(new BasicNameValuePair("dollId",String.valueOf(dollId)));
		params.add(new BasicNameValuePair("token",token));
		params.add(new BasicNameValuePair("gotDoll","0"));
		params.add(new BasicNameValuePair("state","1"));
		 Thread history = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(10000);
					HttpClientUtil.getInstance().executeByPOST(endRoundUrl, params);
					//logger.info("未正常结束补游戏记录");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		 });
		 	history.start();
		}
	}

	@Override
	public void onClose(String token,Integer userId,Integer dollId) throws ClientProtocolException {
		
		//String endGameUrl = propFileMgr.getProperty("webapi.endGame");
		String endGameUrl = gameProperties.getEndGame();
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("memberId",String.valueOf(userId)));
		params.add(new BasicNameValuePair("dollId",String.valueOf(dollId)));
		params.add(new BasicNameValuePair("token",token));
				try {
					HttpClientUtil.getInstance().executeByPOST(endGameUrl, params);
					//logger.info("结束游戏更新状态");
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				}
		process.onClose(userId, dollId);
	}

	@Override
	public synchronized boolean standMachine(String userId, Integer dollId) {
		return process.standMachine(userId, dollId);
	}
	
	@Override
	public void onDollCatch(Integer userId,String catchFlag) {
		//redisUtil.setString(RedisKeyGenerator.getUserGameCatch(userId), catchFlag, 60*2 );
	}
	/**
	 * 补发下抓
	 */
	@Override
	public boolean leaveClaw(Integer userId,Integer dollId) {
		return process.hasGameLock(userId, dollId, GameProcessEnum.GAME_CLAW);
	}
	/**
	 * 补扣费
	 */
	@Override
	public boolean leaveConsume(Integer userId,Integer dollId) {
		return process.hasGameLock(userId, dollId, GameProcessEnum.GAME_CHARGE_HISTORY);
	}
	/**
	 * 补游戏记录
	 */
	@Override
	public boolean leaveHistory(Integer userId,Integer dollId) {
		return process.hasGameLock(userId, dollId, GameProcessEnum.GAME_HISTORY);
	}
	
	@Override
	public void addClaw(Integer userId,Integer dollId) {
		process.addCountGameLock(userId ,dollId ,GameProcessEnum.GAME_CLAW);//计数++
	}
	
	/**
	 * 开始游戏
	 */
	@Override
	public void onOpen(String userId, Integer dollId) {
		process.onOpen(userId, dollId);
	}

	@Override
	public String onMessage(boolean newMachineType,String message, Integer userId, Integer dollId,String device) {
		 String IotMessage = "";
		 if (newMachineType) {
			 StringBuilder sb = new StringBuilder();  
			 if ("coin".equals(message)) {
				 if(process.canPlay(dollId)) {//可以玩
					 if(!process.onCoin(userId, dollId)) {//投币防重复处理
						 return "";
					 };
				 } else {
					return "维修中";
				 }
			 }
			 else if ("claw".equals(message)) {
				 message = process.contrClaw(userId, dollId);//获得强弱抓概率
				 //message = "strongClaw";
				 process.onClaw(userId, dollId);
				// try {
				//	Thread.sleep(200);
					//小机器抓取结果bug
				//} catch (InterruptedException e) {
				//	e.printStackTrace();
				//}
			 }
			 sb.append(device).append("|control|").append(message);
			 /*Thread t = new  Thread(new Runnable() {
				@Override
				public void run() {
					try {
					while(true) {
						machineService.sendMsg(sb.toString(),dollId);
						Thread.sleep(500);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
						return;
					}
				}
			});
			t.start();*/
			// MachineUtils.sendMsg(sb.toString(), dollId, userId);
			 return sb.toString();
		 }
		 else {
		switch (message) {
       case "coin":
      	 if(process.canPlay(dollId)) {//可以玩
			 if(!process.onCoin(userId, dollId)) {//投币防重复处理
				 return "";
			 };
			 IotMessage = "{\"control\":\"coin\"}";
		 } else {
			return "维修中";
		 }
           break;
       case "left":
           IotMessage = "{\"control\":\"left\"}";
           break;
       case "right":
           IotMessage = "{\"control\":\"right\"}";
           break;
       case "forward":
           IotMessage = "{\"control\":\"forward\"}";
           break;
       case "backward":
           IotMessage = "{\"control\":\"backward\"}";
           break;
       case "claw":
           IotMessage = "{\"control\":\"claw\"}";
           process.onClaw(userId, dollId);
           break;
       case "stop":
           IotMessage = "{\"control\":\"stop\"}";
           break;
       case "querry":
           IotMessage = "{\"control\":\"querry\"}";
           break;
		}
			return IotMessage;
		 }
	}
	/**
	 * 机器指令过程优化  防止重复指令   延迟错误指令   转发
	 *  ready 产生游戏编号
	 */
	@Override
	public boolean onReceived(String info ,Integer userId,Integer dollId) {
		//System.out.println("收到机器指令info:"+info);
		if (info==null || "".equals(info)) {
			return false;
		}
		if (info.indexOf("idle")>0) {
			System.out.println("==========向["+dollId+"]转发机器指令idle:"+info);
			return process.getIdle(userId,dollId);
       }
		if (info.indexOf("ready")>0) {
			System.out.println("==========向["+dollId+"]转发机器指令ready:"+info);
			return process.getReady(userId,dollId);
       }
		if (info.indexOf("gotToy")>0) {
			System.out.println("==========向["+dollId+"]转发机器指令gotToy:"+info);
			return process.getCatch(userId,dollId);
		}
		if (info.indexOf("claw")>0) {
			System.out.println("==========向["+dollId+"]转发机器指令claw:"+info);
			return process.getClaw(userId,dollId);
		}
		return true;
	}


	public GameBaseProperties getGameProperties() {
		return gameProperties;
	}

	public void setGameProperties(GameBaseProperties gameProperties) {
		this.gameProperties = gameProperties;
	}

	public RedisService getRedisUtil() {
		return redisUtil;
	}

	public void setRedisUtil(RedisService redisUtil) {
		this.redisUtil = redisUtil;
	}
	
	
}
