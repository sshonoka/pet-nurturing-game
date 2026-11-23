// 2025オブジェクト指向設計演習 最終課題 j224127　斎藤帆乃夏
package PetNurturing; // (1)
import java.io.Serializable; // (2)

// ペットの状態（データ）を保持・管理するクラス

public class Pet implements Serializable { // (3)
    private String name; // (4)
    private int hunger = 5;    // 満腹度（0-10）(5)
    private int happiness = 5; // 幸福度（0-10）
    private int age = 0;       // 年齢

    public Pet(String name) { // (6)
        this.name = name;
    }

    // ごはん：満腹度が上がる (7)
    public void eat() {
        hunger += 3;
        if (hunger > 10) hunger = 10;
    }

    // 遊ぶ：幸福度が上がるが、お腹が減る (8)
    public void play() {
        if (hunger > 0) {
            happiness += 3;
            if (happiness > 10) happiness = 10;
            
            hunger -= 2;
            if (hunger < 0) hunger = 0;
        }
    }

    // 1日経過：年齢が増え、お腹が減り、幸福度も下がる (9)
    public void sleep() {
        age++;
        hunger -= 2;
        if (hunger < 0) hunger = 0;
        happiness -= 1;
        if (happiness < 0) happiness = 0;
    }

    // ゲッター (10)
    public String getName() { return name; }
    public int getHunger() { return hunger; }
    public int getHappiness() { return happiness; }
    public int getAge() { return age; }


    // ペットが生きているか判定
    public boolean isDead() {
        return (hunger <= 0 || happiness <= 0);
    }

}