package ibf2022.batch2.ssf.frontcontroller.model;

import java.util.Random;

public class Captcha {
    private String operator;
    private int firstNum;
    private int secNum;
    private int answer = 0;

    public Captcha() {
        this.operator = getOperatorMethod();
        this.firstNum = getRandNum();
        this.secNum = getRandNum();
        this.answer = getCaptchaAnswer();
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public int getFirstNum() {
        return firstNum;
    }

    public void setFirstNum(int firstNum) {
        this.firstNum = firstNum;
    }

    public int getSecNum() {
        return secNum;
    }

    public void setSecNum(int secNum) {
        this.secNum = secNum;
    }

    public int getRandNum() {
        Random rand = new Random();
        int randInt = rand.nextInt(1, 51);
        return randInt;
    }

    public String getOperatorMethod() {
        String[] mathOperators = { "+", "-", "*", "/" };
        Random rand = new Random();
        int randIndex = rand.nextInt(0, 4);
        return mathOperators[randIndex];

    }

    public String toString() {
        String phrase = "";
        if (this.getFirstNum() > this.getSecNum()) {
            phrase = "What is %d %s %d?".formatted(this.getFirstNum(), this.getOperator(), this.getSecNum());
        } else {
            phrase = "What is %d %s %d?".formatted(this.getSecNum(), this.getOperator(), this.getFirstNum());
        }
        return phrase;
    }

    public int getCaptchaAnswer() {
        int num1 = 0;
        int num2 = 0;
        int capAnswer = 0;
        if (this.getFirstNum() > this.getSecNum()) {
            num1 = this.getFirstNum();
            num2 = this.getSecNum();
        } else {
            num1 = this.getSecNum();
            num2 = this.getFirstNum();
        }
        switch (this.getOperator()) {
            case "+":
                capAnswer = num1 + num2;
                break;
            case "-":
                capAnswer = num1 - num2;
                break;
            case "*":
                capAnswer = num1 * num2;
                break;
            case "/":
                capAnswer = num1 / num2;
                break;
            default:
                capAnswer = 0;
        }
        // this.answer = capAnswer;
        return capAnswer;
    }
}
