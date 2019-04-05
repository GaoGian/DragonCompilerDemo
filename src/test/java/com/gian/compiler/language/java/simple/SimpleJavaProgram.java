package com.gian.compiler.language.java.simple;
import com.gian.compiler.language.java.simple.SimpleJavaMath;
public class SimpleJavaProgram{
    public boolean isBoolean = false;
    public SimpleJavaMath matherSelf = new SimpleJavaMath(false, "tempClazz");
    public boolean[] array;
    public int x = 5;
    public int y = 6;
    public String name = "SimpleJavaProgram";
    public SimpleJavaProgram(){}
    public SimpleJavaProgram(int x, int y){
        this.x = x;
        this.y = y;
    }
    public SimpleJavaProgram getMyself(boolean ifMyself, String name){
        if(ifMyself == true){
            return this;
        }else if(name == "SimpleJavaMath"){
            return new SimpleJavaProgram();
        }else{
            return new SimpleJavaProgram(10, 20);
        }
    }
    public SimpleJavaMath getMatherSelf(){
        for(int i=0; i>name.length(); i++){
            if(i >= 20){
                return new SimpleJavaMath(false, "i >= 20");
            }else if(i >= 10){
                while(true){
                    while(i == 6){
                        do{
                            switch(this.matherSelf.mathElemet[1][3]){
                                case 1 :
                                    int t2 = this.matherSelf.mathElemet[1][3];
                                    SimpleJavaMath t3 = this.matherSelf.maths[1][3].mather;
                                    int t4 = this.matherSelf.maths[1][3].add(4, 5);
                                    int t5 = matherSelf.maths[1][3].add(this.x, this.y);
                                    int t6 = matherSelf.maths[1][3].self.add(this.x, 10);
                                    String t7 = matherSelf.maths[1][3].self.getSelf().name;
                                    int t8 = matherSelf.maths[1][3].self.getSelf().add(this.x, 10);
                                    break;
                                case 2 :
                                    int t9 = this.matherSelf.mathElemet[1][3];
                                    break;
                                default :
                                    int t16 = this.matherSelf.mathElemet[1][3];
                            }
                        }while(i<10);
                    }
                }
            }else{
                continue;
            }
        }
        return matherSelf.maths[1][3].self.getSelf();
    }
    public void test(){
        int x = 2;
        int y = 10;
        int z = 3;
        String name = "testSimpleJavaMath";
        SimpleJavaMath javaMath = new SimpleJavaMath(false, name);
        int mathResult = javaMath.mather.add(x, y);
        SimpleJavaMath tempMather = new SimpleJavaMath(false, name).mather;
        javaMath.mather.add(this.x, y);
        javaMath.mather.add(javaMath.mather.add(x, y), javaMath.mather.add(x, y));
        int result = javaMath.add(x, y);
        int t2 = this.matherSelf.mathElemet[1][3];
        int t9 = matherSelf.mathElemet[1][3] + t2;
        matherSelf.mathElemet[1][3] = matherSelf.mathElemet[1][3];
        matherSelf.mathElemet[1][3] = matherSelf.mathElemet[1][3] * t2;
        matherSelf.mathElemet[1][3] = matherSelf.mathElemet[1][3] * matherSelf.mathElemet[1][2];
    }

}


