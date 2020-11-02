package SimBlock.swing;

import SimBlock.settings.NetworkConfiguration;
import SimBlock.settings.SimulationConfiguration;
import SimBlock.simulator.Main;
import sun.plugin2.message.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WinFrame extends JFrame {    //
    //  Define the variables required by java swing
    private JLabel LNUM_OF_NODES;
    private JLabel LINTERVAL;
    private JLabel LAVERAGE_MINING_POWER;
    private JLabel LSTDEV_OF_MINING_POWER;
    private JLabel LAVERAGE_COINS;
    private JLabel LSTDEV_OF_COINS;
    private JLabel LSTAKING_REWARD;
    private JLabel LENDBLOCKHEIGHT;
    private JLabel LBLOCKSIZE;

    private JLabel LLATENCY_2019;
    private JLabel LDOWNLOAD_BANDWIDTH_2019;
    private JLabel LUPLOAD_BANDWIDTH_2019;
    private JLabel LREGION_DISTRIBUTION_BITCOIN_2019;
    private JLabel Lpartition_region;

    private JTextField TNUM_OF_NODES;
    private JTextField TINTERVAL;
    private JTextField TAVERAGE_MINING_POWER;
    private JTextField TSTDEV_OF_MINING_POWER;
    private JTextField TAVERAGE_COINS;
    private JTextField TSTDEV_OF_COINS;
    private JTextField TSTAKING_REWARD;
    private JTextField TENDBLOCKHEIGHT;
    private JTextField TBLOCKSIZE;

    private JTextArea TLATENCY_2019;
    private JTextField TDOWNLOAD_BANDWIDTH_2019;
    private JTextField TUPLOAD_BANDWIDTH_2019;
    private JTextField TREGION_DISTRIBUTION_BITCOIN_2019;
    private JTextField Tpartition_region;


    private JButton btn;

    private JLabel msg;
    private WinFrame outer;
    public WinFrame(String title){    // Define the framework used by java swing
        outer = this;
        this.setLayout(new FlowLayout(0,10,10));  // define layout configuration

        // Pre-defined parameters and initial values
        LNUM_OF_NODES = new JLabel("NUM_OF_NODES");
        TNUM_OF_NODES = new JTextField("100",75);
        LINTERVAL = new JLabel("INTERVAL");
        TINTERVAL = new JTextField("60000",70);
        LAVERAGE_MINING_POWER = new JLabel("AVERAGE_MINING_POWER");
        TAVERAGE_MINING_POWER = new JTextField("500",70);
        LSTDEV_OF_MINING_POWER = new JLabel("STDEV_OF_MINING_POWER");
        TSTDEV_OF_MINING_POWER = new JTextField("10",70);
        LAVERAGE_COINS = new JLabel("AVERAGE_COINS");
        TAVERAGE_COINS = new JTextField("200",70);
        LSTDEV_OF_COINS = new JLabel("STDEV_OF_COINS");
        TSTDEV_OF_COINS = new JTextField("100",70);
        LSTAKING_REWARD = new JLabel("(D)STAKING_REWARD");
        TSTAKING_REWARD = new JTextField("0.01",70);
        LENDBLOCKHEIGHT = new JLabel("ENDBLOCKHEIGHT");
        TENDBLOCKHEIGHT = new JTextField("20",75);
        LBLOCKSIZE = new JLabel("BLOCKSIZE");
        TBLOCKSIZE = new JTextField("8000",80);
        LLATENCY_2019 = new JLabel("LATENCY_2019");
//        TLATENCY_2019 = new JTextArea(5,70);
//        LDOWNLOAD_BANDWIDTH_2019 = new JLabel("DOWNLOAD_BANDWIDTH_2019");
//        TDOWNLOAD_BANDWIDTH_2019 = new JTextField(70);
//        LUPLOAD_BANDWIDTH_2019 = new JLabel("UPLOAD_BANDWIDTH_2019");
//        TUPLOAD_BANDWIDTH_2019 = new JTextField(70);
        LREGION_DISTRIBUTION_BITCOIN_2019 = new JLabel("(D)REGION_DISTRIBUTION_BITCOIN_2019");
        TREGION_DISTRIBUTION_BITCOIN_2019 = new JTextField("0.4306, 0.3008, 0.1090, 0.1177, 0.0224, 0.0195",65);
        Lpartition_region = new JLabel("THE REGION TO PARTITION FROM 0-5");
        Tpartition_region = new JTextField("2",50);

        btn = new JButton("Submit");
        msg = new JLabel();
        this.add(LNUM_OF_NODES);
        this.add(TNUM_OF_NODES);
        this.add(LINTERVAL);
        this.add(TINTERVAL);
        this.add(LAVERAGE_MINING_POWER);
        this.add(TAVERAGE_MINING_POWER);
        this.add(LSTDEV_OF_MINING_POWER);
        this.add(TSTDEV_OF_MINING_POWER);
        this.add(LAVERAGE_COINS);
        this.add(TAVERAGE_COINS);
        this.add(LSTDEV_OF_COINS);
        this.add(TSTDEV_OF_COINS);
        this.add(LSTAKING_REWARD);
        this.add(TSTAKING_REWARD);
        this.add(LENDBLOCKHEIGHT);
        this.add(TENDBLOCKHEIGHT);
        this.add(LBLOCKSIZE);
        this.add(TBLOCKSIZE);
//        this.add(LLATENCY_2019);
//        this.add(TLATENCY_2019);
//        this.add(LDOWNLOAD_BANDWIDTH_2019);
//        this.add(TDOWNLOAD_BANDWIDTH_2019);
//        this.add(LUPLOAD_BANDWIDTH_2019);
//        this.add(TUPLOAD_BANDWIDTH_2019);
        this.add(LREGION_DISTRIBUTION_BITCOIN_2019);
        this.add(TREGION_DISTRIBUTION_BITCOIN_2019);
        msg.setText("Input format: *，*，*，*，*，*（* reprsents respectibvely\"NORTH_AMERICA\", \"EUROPE\", \"SOUTH_AMERICA\", \"ASIA_PACIFIC\", \"JAPAN\", \"AUSTRALIA\", the sum is one.）");

        this.add(msg);
        this.add(Lpartition_region);
        this.add(Tpartition_region);
        btn.setBackground(Color.CYAN);  // Set button color
        this.add(btn);




        this.setTitle(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Set closed event 设置关闭的事件
        this.setSize(1000, 700);// Set size 设置大小
        this.setVisible(true);// Make the window visible 使窗口可视
        //this.setBackground(Color.pink);    //设置背景色

        btn.addActionListener(new ActionListener() { // Press the button to start the parameter configuration program and pass it to the main program of the blockchain network
                                  public void actionPerformed(ActionEvent e) {
                                      SimulationConfiguration.NUM_OF_NODES = Integer.parseInt(TNUM_OF_NODES.getText());
                                      SimulationConfiguration.INTERVAL =Integer.parseInt(TINTERVAL.getText());
                                      SimulationConfiguration.AVERAGE_MINING_POWER = Integer.parseInt(TAVERAGE_MINING_POWER.getText());
                                      SimulationConfiguration.STDEV_OF_MINING_POWER = Integer.parseInt(TSTDEV_OF_MINING_POWER.getText());

                                      SimulationConfiguration.AVERAGE_COINS = Integer.parseInt(TAVERAGE_COINS.getText());
                                      SimulationConfiguration.STDEV_OF_COINS = Integer.parseInt(TSTDEV_OF_COINS.getText());


                                      SimulationConfiguration.STAKING_REWARD = Double.parseDouble(TSTAKING_REWARD.getText());

                                      SimulationConfiguration.ENDBLOCKHEIGHT = Integer.parseInt(TENDBLOCKHEIGHT.getText());
                                      SimulationConfiguration.BLOCKSIZE = Long.parseLong(TBLOCKSIZE.getText());
                                      String[] s = TREGION_DISTRIBUTION_BITCOIN_2019.getText().split(",");
                                      double[] d = new double[6];
                                      for(int j=0;j<6;j++)
                                          d[j] = Double.parseDouble(s[j]);
                                      System.out.println(d[1]);
                                      NetworkConfiguration.REGION_DISTRIBUTION = d;
                                      NetworkConfiguration.PARTITION_REGION = Integer.parseInt(Tpartition_region.getText());  // partition number input grid

                                      String returnMsg = Main.Click();

                                      JOptionPane.showMessageDialog(outer, returnMsg);
                                      }
                              });

    }
}
