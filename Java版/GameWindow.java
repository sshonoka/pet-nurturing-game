// 2025オブジェクト指向設計演習 最終課題 j224127 斎藤帆乃夏
package PetNurturing;

import javax.swing.*; // (1)
import PetNurturing.Pet;
import java.awt.*;
import java.io.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// メインウィンドウ。GUIの配置とイベント処理を行う。

public class GameWindow extends JFrame { // (2)
    private Pet myPet;
    private PetPanel petPanel;
    private JLabel infoLabel;
    private JButton feedBtn;
    private JButton playBtn;
    private JButton sleepBtn;
    private JButton saveBtn;

    public GameWindow() { // (4)
        setTitle("Pet Nurturing Game");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        int choice = JOptionPane.showConfirmDialog(this, "前回の続きから遊びますか？", "ロード確認", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
        // 保存したデータ(save.dat)を読み込む
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("save.dat"))) {
            myPet = (Pet) ois.readObject();
        } catch (Exception e) {
            // ファイルがない、または読み込めない場合は新規作成へ
            JOptionPane.showMessageDialog(this, "セーブデータが読み込めません。新しく始めます。");
            String name = JOptionPane.showInputDialog(this, "ペットの名前を決めてね！");
            myPet = new Pet(name == null || name.isEmpty() ? "ひつじ" : name);
        }
    } else {
        // 「いいえ」を選んだら新しく作る
        String name = JOptionPane.showInputDialog(this, "ペットの名前を決めてね！");
        myPet = new Pet(name == null || name.isEmpty() ? "ひつじ" : name);
    }

        petPanel = new PetPanel();
        petPanel.setPet(myPet);

        infoLabel = new JLabel("", JLabel.CENTER);

        // ボタンの作成　(6)
        feedBtn = new JButton("ごはん");
        playBtn = new JButton("遊ぶ");
        sleepBtn = new JButton("寝る");
        saveBtn = new JButton("セーブ");

        updateUI();

        // イベント設定 (7)
        feedBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                myPet.eat();
                updateUI();
                if (!myPet.isDead()) {
                    JOptionPane.showMessageDialog(null, myPet.getName() + "に ごはん をあげました！\n「メェ〜！　（おいしい！）」");
                }
            }
        });
        playBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                myPet.play();
                updateUI();
                if (!myPet.isDead()) {
                JOptionPane.showMessageDialog(null, myPet.getName() + "と 一緒に遊びました！\n「メェッ、メェッ！　（たのしいな！）」");
                }
            }
        });
        sleepBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                myPet.sleep();
                updateUI();
                if (!myPet.isDead()) {
                JOptionPane.showMessageDialog(null, myPet.getName() + "は ぐっすり眠りました。\n「スヤスヤ...」");
                }
            }
        });
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveGame();
            }
        });

        // レイアウト (8)
        JPanel ctrlPanel = new JPanel(new GridLayout(4, 1));
        ctrlPanel.add(feedBtn);
        ctrlPanel.add(playBtn);
        ctrlPanel.add(sleepBtn);
        ctrlPanel.add(saveBtn);

        add(infoLabel, BorderLayout.NORTH);
        add(petPanel, BorderLayout.CENTER);
        add(ctrlPanel, BorderLayout.SOUTH);
    }

    // しんでしまったら
    private void updateUI() {
        if (myPet.isDead()) {
            infoLabel.setText(myPet.getName() + "は天国へ行きました...");
            // 全てのボタンを押せなくする
            feedBtn.setEnabled(false);
            playBtn.setEnabled(false);
            sleepBtn.setEnabled(false);

        } else {
            infoLabel.setText(String.format("%s (Age:%d) [満腹:%d 幸せ:%d]", 
                myPet.getName(), myPet.getAge(), myPet.getHunger(), myPet.getHappiness()));
        }
        repaint();
    }

    private void saveGame() { // (10)
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("save.dat"))) {
            oos.writeObject(myPet);
            JOptionPane.showMessageDialog(this, "セーブしました！");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "エラー: " + ex.getMessage());
        }
    }

    public static void main(String[] args) { // (11)
        SwingUtilities.invokeLater(() -> {
            new GameWindow().setVisible(true);
        });
    }
}
