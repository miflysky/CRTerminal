// JanKenPon.java

package com.tieto.crterminal;

import java.util.*;


/*
 * 剪刀石头布 比赛结果判决类
 */
public class JanKenPon
{       
    /**
     * 该方法决定本次出拳的比赛结果
     * @param players： 所有参与者的信息
     * @param winners： 如果本次比赛有人胜出，所有胜出者放置在该列表中；如果为平局，则该List为空 (not null)
     * @param losers ： 如果本次比赛有人胜出，所有失败者放置在该列表中；如果为平局，则该List为空 (not null)
     * @return： JanKenPonResult，平局或者有人胜出
     */
    public static JanKenPonResult judgeCurrentMatchResult(List<JanKenPonPlayer> players, List<JanKenPonPlayer> winners, List<JanKenPonPlayer> losers)
    {
        JanKenPonResult result = JanKenPonResult.Draw;
        final int playersCount = players.size();
        
        // 输入有效性检测
        if (playersCount == 0)
        {
            return result;
        }
        
        // 遍历参与者信息，根据所出拳值分为三组
        List<JanKenPonPlayer> scissorsList = new ArrayList<JanKenPonPlayer>();
        List<JanKenPonPlayer> rockList     = new ArrayList<JanKenPonPlayer>();
        List<JanKenPonPlayer> paperList    = new ArrayList<JanKenPonPlayer>();
        
        for (int i = 0; i < playersCount; ++ i)
        {
            JanKenPonPlayer player = players.get(i);
            if ( JanKenPonValue.Scissors == player.value)
            {
                scissorsList.add(player);
            }
            else if (JanKenPonValue.Rock == player.value)
            {
                rockList.add(player);
            }
            else if (JanKenPonValue.Paper == player.value)
            {
                paperList.add(player);
            }
        } // end for
        
        // 开始判决比赛结果
        do 
        {
            final int scissorsCount = scissorsList.size();
            final int rockCount = rockList.size();
            final int paperCount = paperList.size();
            
            // 如果三种拳值都出现，为平局
            {
                if (scissorsCount * rockCount * paperCount != 0)
                {
                    result = JanKenPonResult.Draw;
                    break;
                }
            }
            
            // 如果所有人出拳相同，则为平局
            {
                if (scissorsCount == playersCount || 
                        rockCount == playersCount || 
                       paperCount == playersCount)
                {
                    result = JanKenPonResult.Draw;
                    break;
                }
            } 
            
            // 只有两种拳值，必能分出胜负
            {
                result = JanKenPonResult.Winner;
                
                // 拳值为  剪刀、石头
                if (paperCount == 0)
                {
                    winners.addAll(rockList);
                    losers.addAll(scissorsList);                    
                    break;
                }
                
                // 拳值为  石头、布
                if (scissorsCount == 0)
                {
                    winners.addAll(paperList);
                    losers.addAll(rockList);
                    break;
                }
                
                // 拳值为  剪刀、布
                if (rockCount == 0)
                {
                    winners.addAll(scissorsList);
                    losers.addAll(paperList);
                    break;                
                }
            }
        } while (false);
        
        
        // 返回结果
        return result;
    }
    
    private static void testJudger1()
    {
        System.out.println("testJudger1() >>> ");
        
        List<JanKenPonPlayer> list = new ArrayList<JanKenPonPlayer>();
        List<JanKenPonPlayer> winners = null;
        List<JanKenPonPlayer> losers = null;
        
        JanKenPonResult result = JanKenPon.judgeCurrentMatchResult(list, winners, losers);
        System.out.println("\tplayers为空，裁判结果为：" + result.toString());
    }
    
    private static void testJudger2()
    {
        System.out.println("testJudger2() >>> ");
        
        List<JanKenPonPlayer> list = new ArrayList<JanKenPonPlayer>();
        List<JanKenPonPlayer> winners = null;
        List<JanKenPonPlayer> losers = null;
        
        JanKenPonPlayer p1 = new JanKenPonPlayer("Lu Jun1", JanKenPonValue.Scissors);
        JanKenPonPlayer p2 = new JanKenPonPlayer("Lu Jun2", JanKenPonValue.Scissors);
        JanKenPonPlayer p3 = new JanKenPonPlayer("Lu Jun3", JanKenPonValue.Scissors);
        JanKenPonPlayer p4 = new JanKenPonPlayer("Lu Jun4", JanKenPonValue.Scissors);
        JanKenPonPlayer p5 = new JanKenPonPlayer("Lu Jun5", JanKenPonValue.Scissors);
        JanKenPonPlayer p6 = new JanKenPonPlayer("Lu Jun6", JanKenPonValue.Scissors);
        
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
        list.add(p5);
        list.add(p6);
        
        JanKenPonResult result = JanKenPon.judgeCurrentMatchResult(list, winners, losers);
        System.out.println("\t所有人都出剪刀，裁判结果为：" + result.toString());
    }
    
    private static void testJudger3()
    {
        System.out.println("testJudger3() >>> ");
        
        List<JanKenPonPlayer> list = new ArrayList<JanKenPonPlayer>();
        List<JanKenPonPlayer> winners = null;
        List<JanKenPonPlayer> losers = null;
        
        JanKenPonPlayer p1 = new JanKenPonPlayer("Lu Jun1", JanKenPonValue.Scissors);
        JanKenPonPlayer p2 = new JanKenPonPlayer("Lu Jun2", JanKenPonValue.Rock);
        JanKenPonPlayer p3 = new JanKenPonPlayer("Lu Jun3", JanKenPonValue.Scissors);
        JanKenPonPlayer p4 = new JanKenPonPlayer("Lu Jun4", JanKenPonValue.Paper);
        JanKenPonPlayer p5 = new JanKenPonPlayer("Lu Jun5", JanKenPonValue.Scissors);
        JanKenPonPlayer p6 = new JanKenPonPlayer("Lu Jun6", JanKenPonValue.Scissors);
        
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
        list.add(p5);
        list.add(p6);
        
        JanKenPonResult result = JanKenPon.judgeCurrentMatchResult(list, winners, losers);
        System.out.println("\t剪刀石头布三个都有，裁判结果为：" + result.toString());
    }
    
    private static void testJudger4()
    {
        System.out.println("testJudger4() >>> ");
        
        List<JanKenPonPlayer> list = new ArrayList<JanKenPonPlayer>();
        List<JanKenPonPlayer> winners = new ArrayList<JanKenPonPlayer>();
        List<JanKenPonPlayer> losers = new ArrayList<JanKenPonPlayer>();
        
        JanKenPonPlayer p1 = new JanKenPonPlayer("Lu Jun1", JanKenPonValue.Scissors);
        JanKenPonPlayer p2 = new JanKenPonPlayer("Lu Jun2", JanKenPonValue.Rock);
        JanKenPonPlayer p3 = new JanKenPonPlayer("Lu Jun3", JanKenPonValue.Scissors);
        JanKenPonPlayer p4 = new JanKenPonPlayer("Lu Jun4", JanKenPonValue.Rock);
        JanKenPonPlayer p5 = new JanKenPonPlayer("Lu Jun5", JanKenPonValue.Scissors);
        JanKenPonPlayer p6 = new JanKenPonPlayer("Lu Jun6", JanKenPonValue.Scissors);
        
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
        list.add(p5);
        list.add(p6);
        
        JanKenPonResult result = JanKenPon.judgeCurrentMatchResult(list, winners, losers);
        System.out.println("\t只有剪刀石头，裁判结果为：" + result.toString());
        if (result == JanKenPonResult.Winner)
        {
            for (int i = 0; i < winners.size(); ++ i)
            {
                JanKenPonPlayer player = winners.get(i);
                System.out.println("\t name:" + player.name + ", move:" + player.value.toString() + "\tWINNER");
            }
            
            for (int i = 0; i < losers.size(); ++ i)
            {
                JanKenPonPlayer player = losers.get(i);
                System.out.println("\t name:" + player.name + ", move:" + player.value.toString() + "\tLOSER");
            }
        }        
    }
    
    private static void testJudger5()
    {
        System.out.println("testJudger5() >>> ");
        
        List<JanKenPonPlayer> list = new ArrayList<JanKenPonPlayer>();
        List<JanKenPonPlayer> winners = new ArrayList<JanKenPonPlayer>();
        List<JanKenPonPlayer> losers = new ArrayList<JanKenPonPlayer>();
        
        JanKenPonPlayer p1 = new JanKenPonPlayer("Lu Jun1", JanKenPonValue.Scissors);
        JanKenPonPlayer p2 = new JanKenPonPlayer("Lu Jun2", JanKenPonValue.Paper);
        JanKenPonPlayer p3 = new JanKenPonPlayer("Lu Jun3", JanKenPonValue.Scissors);
        JanKenPonPlayer p4 = new JanKenPonPlayer("Lu Jun4", JanKenPonValue.Paper);
        JanKenPonPlayer p5 = new JanKenPonPlayer("Lu Jun5", JanKenPonValue.Scissors);
        JanKenPonPlayer p6 = new JanKenPonPlayer("Lu Jun6", JanKenPonValue.Scissors);
        
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
        list.add(p5);
        list.add(p6);
        
        JanKenPonResult result = JanKenPon.judgeCurrentMatchResult(list, winners, losers);
        System.out.println("\t只有剪刀和布，裁判结果为：" + result.toString());
        if (result == JanKenPonResult.Winner)
        {
            for (int i = 0; i < winners.size(); ++ i)
            {
                JanKenPonPlayer player = winners.get(i);
                System.out.println("\t name:" + player.name + ", move:" + player.value.toString() + "\tWINNER");
            }
            
            for (int i = 0; i < losers.size(); ++ i)
            {
                JanKenPonPlayer player = losers.get(i);
                System.out.println("\t name:" + player.name + ", move:" + player.value.toString() + "\tLOSER");
            }
        }        
    }
    
    private static void testJudger6()
    {
        System.out.println("testJudger6() >>> ");
        
        List<JanKenPonPlayer> list = new ArrayList<JanKenPonPlayer>();
        List<JanKenPonPlayer> winners = new ArrayList<JanKenPonPlayer>();
        List<JanKenPonPlayer> losers = new ArrayList<JanKenPonPlayer>();
        
        JanKenPonPlayer p1 = new JanKenPonPlayer("Lu Jun1", JanKenPonValue.Rock);
        JanKenPonPlayer p2 = new JanKenPonPlayer("Lu Jun2", JanKenPonValue.Paper);
        JanKenPonPlayer p3 = new JanKenPonPlayer("Lu Jun3", JanKenPonValue.Rock);
        JanKenPonPlayer p4 = new JanKenPonPlayer("Lu Jun4", JanKenPonValue.Paper);
        JanKenPonPlayer p5 = new JanKenPonPlayer("Lu Jun5", JanKenPonValue.Rock);
        JanKenPonPlayer p6 = new JanKenPonPlayer("Lu Jun6", JanKenPonValue.Paper);
        
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
        list.add(p5);
        list.add(p6);
        
        JanKenPonResult result = JanKenPon.judgeCurrentMatchResult(list, winners, losers);
        System.out.println("\t只有石头和布，裁判结果为：" + result.toString());
        if (result == JanKenPonResult.Winner)
        {
            for (int i = 0; i < winners.size(); ++ i)
            {
                JanKenPonPlayer player = winners.get(i);
                System.out.println("\t name:" + player.name + ", move:" + player.value.toString() + "\tWINNER");
            }
            
            for (int i = 0; i < losers.size(); ++ i)
            {
                JanKenPonPlayer player = losers.get(i);
                System.out.println("\t name:" + player.name + ", move:" + player.value.toString() + "\tLOSER");
            }
        }        
    }
    
    public static void main(String[] args)
    {
        System.out.println("JanKenPon Class internal testing codes>>>>>>> ");
        System.out.println("开始剪刀石头布吧");
        JanKenPon.testJudger1();
        JanKenPon.testJudger2();
        JanKenPon.testJudger3();
        JanKenPon.testJudger4();
        JanKenPon.testJudger5();
        JanKenPon.testJudger6();
        return;
    }
}
