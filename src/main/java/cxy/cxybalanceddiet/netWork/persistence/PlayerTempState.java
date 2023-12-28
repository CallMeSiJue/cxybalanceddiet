package cxy.cxybalanceddiet.netWork.persistence;

/**
 * 玩家的体感温度
 */
public class PlayerTempState {


    public int playerTempStatus = 0;
    /**
     * 口渴值
     */
    public double thirstValue = 20;


    public void reset() {

        this.playerTempStatus = 0;
        this.thirstValue = 20;

    }


}
