// JanKenPonPlayer.java

package com.tieto.crterminal.model;

/*
 * 参与比赛者的信息，主要是用户名和其所出的值
 */
public class JanKenPonPlayer
{
    // 默认构造函数
    public JanKenPonPlayer()
    {
        name = "";
        value = JanKenPonValue.Scissors;
    }

    // 构造函数
    public JanKenPonPlayer(String username, int move)
    {
        name = username;
        value = move;
    }

    public String name;            // user id or name    
    public int value;   // his/her value of current match
}
