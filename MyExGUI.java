package visibleArithmeticExercise;

import java.awt.*;
import java.awt.event.*;
import javax.script.ScriptException;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MyExGUI {
    //定义窗口栏
    private JFrame mainWindow = new JFrame("四则运算练习软件");

    //定义面板

    //标签类
    private JPanel selectPanel = new JPanel();
    private JPanel mainPanel = new JPanel();
    private JPanel commandP = new JPanel();

    //按钮类
    private JButton JBRedo = new JButton("重做");
    private JButton JBStart = new JButton("开始做题");

    //文本框类
    private JLabel JLUsersName = new JLabel("请输入你的用户名：");
    private JLabel JLChooseOp = new JLabel("设置皮肤：");
    private JLabel JLNumberDigit = new JLabel("暂未开发：");
    private JLabel JLBAnsTip = new JLabel("输入答案");

    //下拉框内容
    private JTextField JTFUserName = new JTextField(8);//10的单位不是px 而是指定列数
    private String[] operationType = {"原色","浅蓝","灰色","橙色","粉色"};
    private String[] numberOfDigitType = {"1","2"};
    private JComboBox<String> JCBOperationSelect = new JComboBox<String>(operationType);//JComboBox 泛型 需要加上<E>
    private JComboBox<String> JCBNumberOfDigit = new JComboBox<String>(numberOfDigitType);

    //显示题目的JLabel
    private JLabel[] JLBQuestions= new JLabel[10];
    //显示正确答案的JLabel
    private JLabel[] JLBAnswers = new JLabel[10];
    //显示用户答案是否正确的JLabel
    private JLabel[] JLBIsTrue = new JLabel[10];

    private JTextField[] JTFUsersAnswer = new JTextField[10];//定义变量时需要赋初值，不然会出现空指针异常问题


    //设置Font
    private Font buttonFont = new Font("微软雅黑",Font.PLAIN,16);
    private Font JLBFont = new Font("微软雅黑",Font.BOLD,18);
    private Font JTFFont = new Font("微软雅黑",Font.PLAIN,18);
    private Font JLBAnsFont = new Font("微软雅黑",Font.PLAIN,16);


    //用户答案数组
    private String[] userAnswer = new String[10];

    //标签初始化
    private int scores ;
    private JLabel JLBScores = new JLabel("你的成绩为:");
    private JButton JBOpenFile = new JButton("查看记录");
    private String chara = "+";
    private File pFile = new File("四则运算记录");
    private int usedTime;
    boolean runFlag = false;//runFlag默认为false
    private JPanel PTime = new JPanel();
    private JLabel JLBRemainTime = new JLabel("剩余时间：");
    private JTextField JTFWtime = new JTextField("120");
    private JLabel JLBTime = new JLabel("用时：");
    List<String> expressionList ;


    //倒计时线程
    class LimitTime extends Thread{
        public void run()
        {
            runFlag = true;
            int i = 120;
            usedTime = 0;
            while(runFlag && i >= 0)
            {
                JTFWtime.setText(""+i);
                try {
                    sleep(1000);
                    usedTime++;
                } catch (InterruptedException e) {
                    JFrame jf = new JFrame();
                    JOptionPane.showMessageDialog(jf,"出现了未知问题，请重启程序");
                }
                i--;
            }
            //runFlag = false;
            for(int j = 0;j < 10;j++)
            {
                if(JTFUsersAnswer[j].getText().equals(""))
                {
                    JTFUsersAnswer[j].setText("0");
                }
            }
                printAnswer();//倒计时结束，则调用printAnswer()方法
            JBStart.setText("开始做题");
            JLBTime.setText("用时："+usedTime);
        }
    }
    //布局与监听
    public MyExGUI() throws IOException {

        //布局用户名&选择面板
        selectPanel.setPreferredSize(new Dimension(700,50));
        //selectPanel.setLayout(new GridLayout(1,6,25,20));
        JLUsersName.setFont(JLBFont);
        selectPanel.add(JLUsersName);
        JTFUserName.setFont(JLBFont);
        selectPanel.add(JTFUserName);
        JLChooseOp.setFont(JLBFont);
        selectPanel.add(JLChooseOp);
        JCBOperationSelect.setPreferredSize(new Dimension(50,25));       //当selectPanel.setLayout那句存在时，这里的设置无效
        selectPanel.add(JCBOperationSelect);
        JLNumberDigit.setFont(JLBFont);
        selectPanel.add(JLNumberDigit);
        JCBNumberOfDigit.setPreferredSize(new Dimension(50,25));
        selectPanel.add(JCBNumberOfDigit);

        //布局主面板
        mainPanel.setPreferredSize(new Dimension(700,400));

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints GBC = new GridBagConstraints();
        GBC.weightx = 1;//加上这两行之后文本框的大小会和不加时不同 因为它描述的是随面板变化的情况 而现在面板的设定值是800*500 因此不同(不以设定大小为默认值)
        GBC.weighty = 1;
        //GBC.fill = GridBagConstraints.BOTH;//weightx描述的是网格的大小随面板大小变化，组件的大小并不会随之变化 要使组件随之变化需要控制它对所在位置的填充方式
        //GBC.insets = new Insets(1,1,2,2);
        GBC.gridx = 1;
        GBC.gridy = 0;
        GBC.anchor = GridBagConstraints.WEST;
        gridbag.setConstraints(JLBAnsTip, GBC);
        JLBAnsTip.setFont(JLBFont);
        mainPanel.add(JLBAnsTip);

        GBC.gridx = 2;


        GBC.gridx = 4;
        GBC.gridwidth = 2;
        GBC.anchor = GridBagConstraints.CENTER;
        gridbag.setConstraints(JLBScores, GBC);
        JLBScores.setFont(JLBFont);
        mainPanel.add(JLBScores);

        for(int i = 0;i < 5;i++)
        {
            //设置标签字体大小
            JLBQuestions[i] = new JLabel("点击开始做题显示题目");
            JLBQuestions[i].setFont(JLBFont);
            JTFUsersAnswer[i] = new JTextField(5);                      //一定要加这行 不然会出现空指针错误
            JTFUsersAnswer[i].setFont(JTFFont);
            JLBAnswers[i] = new JLabel("");
            JLBAnswers[i].setFont(JLBAnsFont);
            JLBIsTrue[i] = new JLabel("");
            JLBIsTrue[i].setFont(JLBAnsFont);


           //布局左边问题标签和答案标签
            GBC.gridwidth = 1;
            GBC.gridx = 0;
            GBC.gridy = 2*i+1;
            GBC.anchor = GridBagConstraints.EAST;
            gridbag.setConstraints(JLBQuestions[i], GBC);
            mainPanel.add(JLBQuestions[i]);
            GBC.anchor = GridBagConstraints.CENTER;
            GBC.gridy = 2*i+2;
            GBC.gridwidth = 2;
            gridbag.setConstraints(JLBAnswers[i], GBC);
            mainPanel.add(JLBAnswers[i]);


            //布局左边用户答案输入框和正错提醒标签
            GBC.gridwidth = 1;
            GBC.gridx = 1;
            GBC.gridy = 2*i+1;
            GBC.anchor = GridBagConstraints.WEST;
            gridbag.setConstraints(JTFUsersAnswer[i],GBC);
            mainPanel.add(JTFUsersAnswer[i]);
            GBC.gridx = 2;
            GBC.gridy = 2*i+1;
            gridbag.setConstraints(JLBIsTrue[i], GBC);
            mainPanel.add(JLBIsTrue[i]);
        }

        for(int i = 5;i < 10;i++)
        {
            //设置标签字体大小
            JLBQuestions[i] = new JLabel("点击开始做题显示题目");
            JLBQuestions[i].setFont(JLBFont);
            JTFUsersAnswer[i] = new JTextField(5);                      //一定要加这行 不然会出现空指针错误
            JTFUsersAnswer[i].setFont(JTFFont);

            JLBAnswers[i] = new JLabel("");
            JLBAnswers[i].setFont(JLBAnsFont);
            JLBIsTrue[i] = new JLabel("");
            JLBIsTrue[i].setFont(JLBAnsFont);


            //布局右边问题标签和答案标签
            GBC.gridx = 4;
            GBC.gridy = 2*i-9;
            GBC.anchor = GridBagConstraints.EAST;
            gridbag.setConstraints(JLBQuestions[i], GBC);
            mainPanel.add(JLBQuestions[i]);
            GBC.anchor = GridBagConstraints.CENTER;
            GBC.gridy = 2*i-8;
            GBC.gridwidth = 2;
            gridbag.setConstraints(JLBAnswers[i], GBC);
            mainPanel.add(JLBAnswers[i]);


            //布局右边用户答案输入框和正错提醒标签
            GBC.gridwidth = 1;
            GBC.gridx = 5;
            GBC.gridy = 2*i-9;
            GBC.anchor = GridBagConstraints.WEST;
            gridbag.setConstraints(JTFUsersAnswer[i],GBC);
            mainPanel.add(JTFUsersAnswer[i]);
            GBC.gridx = 6;
            GBC.gridy = 2*i-9;
            gridbag.setConstraints(JLBIsTrue[i], GBC);
            mainPanel.add(JLBIsTrue[i]);
        }
        mainPanel.setLayout(gridbag);

        //布局命令面板
        commandP.setLayout(new FlowLayout(FlowLayout.CENTER,60,20));
        JLBRemainTime.setFont(JLBFont);
        JLBTime.setFont(JLBFont);
        JTFWtime.setFont(JTFFont);
        PTime.setLayout(new FlowLayout(FlowLayout.LEFT,10,20));
        PTime.add(JLBRemainTime);
        PTime.add(JTFWtime);
        PTime.add(JLBTime);
        commandP.add(PTime);
        JBStart.setFont(buttonFont);
        commandP.add(JBStart);
        JBRedo.setFont(buttonFont);
        commandP.add(JBRedo);
        JBOpenFile.setFont(buttonFont);
        commandP.add(JBOpenFile);

        //使用匿名嵌套类的方式注册开始按钮的事件处理监听器对象
        JBStart.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(JBStart.getText().equals("开始做题"))
                        {
                            if(JTFUserName.getText().trim().equals(""))
                            {
                                JFrame nullNameWarning = new JFrame();
                                JOptionPane.showMessageDialog(nullNameWarning,"请输入用户名");//确保用户输入用户名
                            }
                            else{
                                try {
                                    start(); //如果按钮上面的文字是"开始做题"，则调用start()方法出题
                                } catch (ScriptException e1) {
                                    e1.printStackTrace();
                                }
                                JBStart.setText("提交答案");
                                //倒计时线程开始
                                LimitTime t = new LimitTime();
                                t.start();
                            }
                        }
                        else
                        {
                            for(int i = 0;i < 10;i++)
                            {
                                if(JTFUsersAnswer[i].getText().equals(""))
                                {
                                    JTFUsersAnswer[i].setText("0");
                                }
                            }
                            runFlag = false;//将runFlag设置为false（线程就会不再执行while循环里的内容）
                            JLBTime.setText("用时："+usedTime);
                            JBStart.setText("开始做题");

                        }
                    }
                }
        );

        //监听重做按钮
        JBRedo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(JBStart.getText().equals("开始做题"))//若已提交答案 则可以进行重做
                {
                    for(int i = 0;i < 10;i++)
                    {
                        JTFUsersAnswer[i].setText("");

                        JLBAnswers[i].setText("");
                        JLBIsTrue[i].setText("");
                        JLBScores.setText("");
                    }
                    JLBTime.setText("用时：");
                    LimitTime t = new LimitTime();
                    t.start();
                    JBStart.setText("提交答案");
                }
                else//答案未提交 不能重做
                {
                    JFrame notSubmit = new JFrame();
                    JOptionPane.showMessageDialog(notSubmit,"提交后才可以重做！提交前可以直接更改答案！");
                }
            }
        });


        //监听选择皮肤标签
        JCBOperationSelect.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                chara = (String) JCBOperationSelect.getSelectedItem();
                if(chara.equals("浅蓝"))
                {
                    mainPanel.setBackground(Color.CYAN);

                }else if(chara.equals("灰色")){
                    mainPanel.setBackground(Color.GRAY);

                }else if(chara.equals("橙色")){
                    mainPanel.setBackground(Color.orange);

                }else if(chara.equals("原色")){
                    mainPanel.setBackground(Color.white);

                }else{
                    mainPanel.setBackground(Color.PINK);
                }
            }
        });


        //查看以往做题记录的按钮监听器
        JBOpenFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                if(JTFUserName.getText().trim().equals(""))
                {
                    JFrame nullNameWarning = new JFrame();
                    JOptionPane.showMessageDialog(nullNameWarning,"请输入用户名");//确保用户输入用户名
                }
                else{
                    //一般不能实例化一个Runtime对象，应用程序也不能创建自己的Runtime 类实例，但可以通过getRuntime 方法获取当前Runtime运行时对象的引用。一旦得到了一个当前的Runtime对象的引用，就可以调用Runtime对象的方法去控制Java虚拟机的状态和行为。
                    Runtime ce=Runtime.getRuntime();
                    pFile.mkdirs();
                    String filename = JTFUserName.getText()+".his";
                    File aUserRec = new File(pFile,filename);
                    if(aUserRec.exists())
                    {
                        try{
                            //ce.exec("cmd   /c   start  "+aUserRec.getAbsolutePath());//这样是不能打开的 因为没有东西能打开.his文件 会跳出来搜索应用商店
                            ce.exec("notepad.exe "+aUserRec.getAbsolutePath());

                        }catch(IOException exc){
                            exc.printStackTrace();
                        }
                    }else{
                        JFrame nullFileWarning = new JFrame();
                        JOptionPane.showMessageDialog(nullFileWarning,"该用户暂无记录!");
                    }
                }
            }
        });


        //尽量把主窗体的设置都放到最后
        mainWindow.add(selectPanel,BorderLayout.NORTH);
        mainWindow.add(mainPanel,BorderLayout.CENTER);
        mainWindow.add(commandP, BorderLayout.SOUTH);
        mainWindow.pack();
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(800,500);//设置窗体大小
        mainWindow.setLocationRelativeTo(null);//将窗口置于屏幕中间
        mainWindow.setVisible(true);//设置为可见 要放在最后 放在前面则只能看见用户名和选择面板 主面板等需要拖动窗口大小才能看见


        //打开userLisr文件，获取最后使用的用户名
        FileReader readUserName=new FileReader("C:\\Users\\KITTY\\IdeaProjects\\untitled3\\四则运算记录\\userList");
        BufferedReader bufReader = new BufferedReader(readUserName);

        String tempString = null;
        String name=null;
        int line = 1;
        // 一次读入一行，直到读入null为文件结束
        while ((tempString = bufReader.readLine()) != null) {
        // System.out.println("line " + line + ": " + tempString);
            if(tempString!=null){
                name=tempString;
            }
            line++;
        }
        //更改用户名标签为最后一次用户名
        JTFUserName.setText(name);
        JFrame nullFileWarning = new JFrame();
        JOptionPane.showMessageDialog(nullFileWarning,name+"，欢迎回来！");

        bufReader.close();
        readUserName.close();
    }


    //在面板上产生10道随即题目
    public void start() throws ScriptException {
        //清除TextField和答案标签的内容
        CalculateGenerator exercises = new CalculateGenerator();

        expressionList=exercises.produceExercises();
        for (int i = 0; i < 10; i++) {
            JLBQuestions[i].setText(expressionList.get(i));
            JTFUsersAnswer[i].setText("");
            JLBAnswers[i].setText("");
            JLBIsTrue[i].setText("");
            JLBScores.setText("");
            JLBTime.setText("用时：");

        }
    }



    //在面板上显示每题的正确答案、得分和用时，并且将每次做题的记录写入文件
    public void printAnswer() {
         //创建用户文件
         pFile.mkdirs();
         String filename = JTFUserName.getText()+".his";
         String userListName="userList";

         File aUserRec = new File(pFile,filename);
         File usersList=new File(pFile,userListName);
         //创建用户的成绩记录文件
         if(! (aUserRec.exists()))
         {
             try{
                 aUserRec.createNewFile();
             }catch(Exception e){
                 e.printStackTrace();
                 JFrame jf = new JFrame();
                 JOptionPane.showMessageDialog(jf,"用户文件创建失败");
             }
         }


         //创建一个记录所有使用过的用户名文件
         if(! (usersList.exists()))
         {
             try{
                 usersList.createNewFile();
             }catch(Exception e){
                 e.printStackTrace();
                 JFrame jf = new JFrame();
                 JOptionPane.showMessageDialog(jf,"用户文件创建失败");
             }
         }

        //定义CalculateGenerator类的一个对象answer，获取10道题的答案集合
         CalculateGenerator answer=new CalculateGenerator();

         List<String> answerList ; //用来记录正确答案的集合
         answerList=answer.answerList; //赋值

        //成绩初始值为100
        scores = 100;

        //对于每道题
        for(int i = 0; i < 10;i++)
        {
            //给用户的答案这一数组赋值（getText的结果为String）
            userAnswer[i] = String.valueOf(Integer.valueOf(JTFUsersAnswer[i].getText()));
            //使正确答案显示在面板上
            JLBAnswers[i].setText("正确答案："+answerList.get(i));
            //在面板上显示答案是否正确
            if(userAnswer[i].equals(answerList.get(i))==true){
                JLBIsTrue[i].setText("答案正确");
            }else{
                JLBIsTrue[i].setText("答案错误");
            }
            //错误则改变字体颜色为红色，正确则改为黑色
            if(JLBIsTrue[i].getText().equals("答案错误"))
            {
                JLBAnswers[i].setForeground(Color.RED);
                JLBIsTrue[i].setForeground(Color.RED);
                scores-=10;
            } else
            {
                JLBAnswers[i].setForeground(Color.BLACK);
                JLBIsTrue[i].setForeground(Color.BLACK);
            }

            //将每道题的正确答案和用户答案写入文件
//            try
//            {
//                PrintWriter out = new PrintWriter(new FileWriter(aUserRec,true));
//
//                out.println("你此次的得分是："+scores+"    "+"所用时间为："+usedTime+"秒");
//                out.close();
//            }catch(FileNotFoundException e){
//                System.err.println("File not found!" );
//            }catch(IOException e2){
//                e2.printStackTrace();
//            }
        }

            //分数标签显示成绩
         JLBScores.setText("你的成绩为："+ scores);
        if(scores==100){
            JFrame nullFileWarning = new JFrame();
            JOptionPane.showMessageDialog(nullFileWarning,"恭喜你答对所有题目！");
        }


            //将得分和用时写入对应的文件
         try
         {
             PrintWriter out = new PrintWriter(new FileWriter(aUserRec,true));
             out.println(JTFUserName.getText());
             out.println("你此次的得分是："+scores+"    "+"所用时间为："+usedTime+"秒");
             out.println("");
             out.close();
             //同时记录当前用户名到userList中
             out = new PrintWriter(new FileWriter(usersList,true));
             out.println(JTFUserName.getText());
             out.close();
         }catch(FileNotFoundException e){
             System.err.println("File not found!" );
         }catch(IOException e2){
             e2.printStackTrace();
         }
     }

}


