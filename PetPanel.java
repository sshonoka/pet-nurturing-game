// 2025オブジェクト指向設計演習 最終課題 j224127　斎藤帆乃夏
package PetNurturing; // (1)

import javax.swing.*; //(2)
import PetNurturing.Pet;
import java.awt.*;

// ペットの姿をGraphicsクラスで描画するクラス

public class PetPanel extends JPanel { //(3)
    private Pet pet; //(4)

    // 画像ファイルを読み込む
    private Image backImg = new ImageIcon("Background.png").getImage();
    private Image petImg = new ImageIcon("Sheep.png").getImage();
    private Image deadImg = new ImageIcon("dead.png").getImage();
    private Image fukidashiImg = new ImageIcon("fukidashi.png").getImage();
    private Image sadIcon = new ImageIcon("sad.png").getImage();

    public void setPet(Pet pet) { // (5)
        this.pet = pet;
    }

    protected void paintComponent(Graphics g) { // (6)
        super.paintComponent(g);
        if (pet == null) return; // (7)

        // 背景  (8)
        g.drawImage(backImg, 0, 0, getWidth(), getHeight(), this);


        // ペット (9)
       if (pet.isDead()) {
            // 死んでいる時の画像
            g.drawImage(deadImg, 50, 150, 150, 150, this);

        } else {
            // 生きている時
            // ひつじを表示
            g.drawImage(petImg, 50, 150, 150, 150, this);

            // 空腹または幸福度が低い場合に吹き出しセットを表示
            if (pet.getHunger() <= 3) {
                drawSpeech(g, "おなかすいた");
            } else if (pet.getHappiness() <= 3) {
                drawSpeech(g, "さびしいな");
            }
        }
    }
    
    // 吹き出し・アイコン・テキストをまとめて描くメソッド
    private void drawSpeech(Graphics g, String text) {
        // 吹き出しの土台
        g.drawImage(fukidashiImg, 140, 20, 180, 140, this);
        
        // 吹き出しの中のアイコン
        g.drawImage(sadIcon, 190, 20, 80, 90, this);
        
        // アイコンの下のテキスト
        g.setColor(Color.BLACK);
        g.drawString(text, 195, 115);
    }
}