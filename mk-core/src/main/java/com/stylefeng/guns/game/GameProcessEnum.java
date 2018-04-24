package com.stylefeng.guns.game;

/**
 * 游戏流程 计数器
 * @author Administrator
 *
 */
public enum GameProcessEnum {
    GAME_CLAW(0, "下抓控制"),
    GAME_CONSUME(1, "扣费控制"),
    GAME_SETTER(2, "抓中娃娃订单控制"),
    GAME_HISTORY(3, "游戏记录保存次数"),
    GAME_CATCH(4,"抓中结果计数"),
    GAME_END(5,"游戏结束计数"),
    GAME_IDLE(6,"机器空闲指令计数"),
    GAME_COIN(7,"用户投币计数"),
    GAME_READY(8,"机器准备好指令计数"),
    GAME_CLAW2(9, "机器下抓回复指令计数"),
    GAME_CHARGE_HISTORY(10, "游戏消费记录计数");

    private final int code;

    private final String desc;

    GameProcessEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


}
