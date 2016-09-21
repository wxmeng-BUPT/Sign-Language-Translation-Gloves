
// Arduino Wire library is required if I2Cdev I2CDEV_ARDUINO_WIRE implementation
// is used in I2Cdev.h
#include "Wire.h"
// I2Cdev and MPU6050 must be installed as libraries, or else the .cpp/.h files
// for both classes must be in the include path of your project
#include "I2Cdev.h"
#include "MPU6050.h"

#define my_Serial Serial
#define ble_Serial Serial1
#define finger0 A0
#define finger1 A1
#define finger2 A2
#define finger3 A3
#define finger4 A6

#define SignNum 32
#define DataNum 64
#define N 3
MPU6050 accelgyro;
int n0,n1,n2,n3,n4;
int sumprev,result,otherresult,resultprev,count;
int16_t ax, ay, az;
int16_t gx, gy, gz;

//training data
int Prev[DataNum][9] = {
    385,502,262,291,248, 1,2,1, 0,//you
    385,502,262,291,248, 1,2,1, 0,//you
    501,269,284,297,257, 1,2,1, 1,//good
    501,269,284,297,257, 1,2,1, 1,//good   
    385,502,262,291,248, 1,1,0, 2,//he
    385,502,262,291,248, 1,1,0, 2,//he
    348,510,481,334,300, 1,1,0, 3,//yes
    348,510,481,334,300, 1,1,0, 3,//yes
    317,508,472,506,512, 0,1,0, 4,//bl
    317,508,472,506,512, 0,1,0, 4,//b   
    338,508,475,353,303, 0,1,0, 5,//u
    338,508,475,353,303, 0,1,0, 5,//u
    489,502,365,341,300, 0,1,0, 6,//p
    489,502,365,341,300, 0,1,0, 6,//p
    493,429,283,300,271, 0,1,0, 7,//t
    454,376,254,295,265, 0,1,0, 7,//t
    300,303,281,286,272, 0,1,0, 8,//de
    300,303,281,286,272, 0,1,0, 8,//de
    354,306,475,506,504, 1,2,1, 9,//e
    354,306,475,506,504, 1,2,1, 9,//e
    355,507,481,346,317, 1,2,1, 10,//f
    355,507,481,346,317, 1,2,1, 10,//f
    382,501,292,301,290, 0,1,0, 11,//one
    382,501,292,301,290, 0,1,0, 11,//one
    482,308,290,335,502, 0,1,0, 12,//six
    482,308,290,335,502, 0,1,0, 12,//six
    485,498,268,299,268, 0,1,0, 13,//eight
    485,498,268,299,268, 0,1,0, 13,//eight
    385,502,262,291,248, 0,2,1, 14,//me
    385,502,262,291,248, 0,2,1, 14,//me
    317,508,472,506,512, 0,1,1, 15,//student1
    317,508,472,506,512, 0,1,1, 15,//student1
    501,269,284,297,257, 0,1,0, 16,//a
    501,269,284,297,257, 0,1,0, 16,//a   
    503,465,430,463,462, 1,2,1, 17,//come
    504,464,431,466,458, 1,2,1, 17,//come
    482,308,290,335,502, 1,1,0, 18,//also
    482,308,290,335,502, 1,1,0, 18,//also
    482,308,290,335,502, 1,2,1, 19,//go
    482,308,290,335,502, 1,2,1, 19,//go
    400,508,472,506,512, 1,2,2, 20,//now
    400,508,472,506,512, 1,2,2, 20,//now
    400,508,472,506,512, 1,1,0, 21,//+20-shenme/student2
    400,508,472,506,512, 1,1,0, 21,//+20-shenme/student2
    385,502,262,291,248, 1,1,2, 22,//hen
    385,502,262,291,248, 1,1,2, 22,//hen
    434,506,466,503,506, 0,1,0, 23,//bu1
    434,506,466,503,506, 0,1,0, 23,//bu1
    434,506,466,503,506, 1,2,1, 24,//bu2
    434,506,466,503,506, 1,2,1, 24,//bu2
    434,506,466,503,506, 0,0,1, 25,//sorry1
    434,506,466,503,506, 0,0,1, 25,//sorry1
    343,332,285,329,503, 1,2,1, 26,//sorry2
    343,332,285,329,503, 1,2,1, 26,//sorry2
    338,508,475,353,303, 0,1,1, 27,//5+27or
    338,508,475,353,303, 0,1,1, 27,//5+27or
    317,429,283,300,271, 1,2,1, 28,//just
    317,376,254,295,265, 1,2,1, 28,//just
    356,505,253,303,263, 2,1,1, 29,//here
    356,505,253,303,263, 2,1,1, 29,//here
    483,327,276,345,502, 2,1,0, 30,//be
    480,324,271,342,502, 2,1,0, 30,//be
    503,512,481,323,343, 1,1,0, 31,//see
    503,512,482,325,346, 1,1,0, 31//see

};

char* SignName[SignNum]={
    "you","good","he","yes","b","u","p","t","de","e",
    "f","one","six","eight","me","student","a","come","also","go",
    "now","shenme2","hen","bu1","bu2","sorry1","sorry2","5+27or","just","here",
    "be","see"
};

//abs function
int abst(int x){
  if(x>=0)return x;else return -x;
}

//abs function for angle 0-9
int absl(int x){
  if(x>9 || x<-9)
    return 9;
  else if(x>=0)
    return x;
  else
    return -x;
}

//Quantification
int trans(int x){
//    if( 450 < x )return 2;
//    else if( 330 < x && x<=450)return 1;
//    else if(x<330)return 0;
//    return -1;
    return x;
}

//k=N KNN Classify algorithm
int Classify(int f0, int f1, int f2,int f3,int f4,int ax,int ay,int az,int swtich){
    //Classify algorithm
    int diff[DataNum] = {0};
    int BucketCount[SignNum]={0};

    //计算距离
    for(int i=0;i<DataNum;i++){
        diff[i] =abst(f0-Prev[i][0])+abst(f1-Prev[i][1])+abst(f2-Prev[i][2])+abst(f3-Prev[i][3])+abst(f4-Prev[i][4])+100*abst(ax-Prev[i][5])+100*abst(ay-Prev[i][6])+100*abst(az-Prev[i][7]);
    }

    //找到距离最短的N组数据的下标
    int mymin = diff[0];
    int minnum[N] = {0};//第N小值的下标
    for(int j=0;j<N;j++){
        for(int i=0;i<DataNum;i++){
            if(diff[i]<mymin){
                mymin = diff[i];
                minnum[j] = i;
            }
        }
        diff[minnum[j]]+=1000;//计算过的标记为极远
        mymin = diff[minnum[j]];
    }

    //桶计数，记录每个分类得票数
    for(int j=0;j<N;j++){
        BucketCount[Prev[minnum[j]][8]]++;
    }
    
    //找到票数最多的分类结果
    int maxnum = 0;
    int mymax = BucketCount[0];
    for(int i=0;i<SignNum;i++){
        if(BucketCount[i]>mymax){
            mymax = BucketCount[i];
            maxnum = i;
        }
    }
    BucketCount[maxnum] = 0;
    
    int othermaxnum = 0;
    mymax = BucketCount[0];
    for(int i=0;i<SignNum;i++){
        if(BucketCount[i]>mymax){
            mymax = BucketCount[i];
            othermaxnum = i;
        }
    }
    if(swtich == 1)return maxnum;
    else return othermaxnum;
}


void setup() {
    // join I2C bus (I2Cdev library doesn't do this automatically)
    Wire.begin();

    // initialize serial communication
    my_Serial.begin(9600);
    ble_Serial.begin(9600);
    // initialize device
    //Serial.println("Initializing I2C devices...");

    accelgyro.initialize();
    //accelgyro.testConnection();
    pinMode(finger0,INPUT);
    pinMode(finger1,INPUT);
    pinMode(finger2,INPUT);
    pinMode(finger3,INPUT);
    pinMode(finger4,INPUT);
    
}

void loop() {
    // read raw accel/gyro measurements from device
    accelgyro.getMotion6(&ax, &ay, &az, &gx, &gy, &gz);

    // these methods (and a few others) are also available
    //accelgyro.getAcceleration(&ax, &ay, &az);
    //accelgyro.getRotation(&gx, &gy, &gz);

    
    n0 = analogRead(finger0);
    n1 = analogRead(finger1);
    n2 = analogRead(finger2);
    n3 = analogRead(finger3);
    n4 = analogRead(finger4);
    result = Classify(n0,n1,n2,n3,n4,(ax/1800+8)/6,(ay/1800+8)/6,(az/1800+8)/6,1);
    otherresult = Classify(n0,n1,n2,n3,n4,(ax/1800+8)/6,(ay/1800+8)/6,(az/1800+8)/6,0);
    if(result == resultprev){
        count ++;
    }
    else{
        count = 0;
    }

    if(count==5){
        count = 0;
        ble_Serial.print(result);
        ble_Serial.print(" ");
        ble_Serial.print(otherresult);
        ble_Serial.print(" ");
        ble_Serial.print((ax/1800+8)/6);// x轴倾角
        ble_Serial.print(" ");
        ble_Serial.print((ay/1800+8)/6);// y轴倾角
        ble_Serial.print(" ");
        ble_Serial.print((az/1800+8)/6);// z轴倾角        
        ble_Serial.print(" ");
        
        my_Serial.print(result);
        my_Serial.print(" ");
        my_Serial.print(otherresult);
        my_Serial.print(" ");
        
        my_Serial.print(trans(n0));//右手大拇指
        my_Serial.print(" ");
        my_Serial.print(trans(n1));//右手食指
        my_Serial.print(" ");
        my_Serial.print(trans(n2));//右手中拇
        my_Serial.print(" ");
        my_Serial.print(trans(n3));//右手无名指
        my_Serial.print(" ");
        my_Serial.print(trans(n4));//右手小拇指
        my_Serial.print(" ");
        
        my_Serial.print((ax/1800+8)/6);//x轴倾角
        my_Serial.print(" ");
        my_Serial.print((ay/1800+8)/6);//y轴倾角
        my_Serial.print(" ");
        my_Serial.print((az/1800+8)/6);//z轴倾角        
        my_Serial.print(" ");
        
        my_Serial.print(absl(gx/1800/5));//x轴角加速度
        my_Serial.print(" ");
        my_Serial.print(absl(gy/1800/5));//y轴角加速度
        my_Serial.print(" ");
        my_Serial.print(absl(gz/1800/5));//z轴角加速度
        
        my_Serial.print(" ");
        my_Serial.println(SignName[result]);
    }
    //sumprev = n0+n1+n2+n3+n4+100*(ax/1800+8)/6+100*(ay/1800+8)/6+100*(az/1800+8)/6+absl(gx/180/2)+absl(gy/180/2)+absl(gz/180/2);
    resultprev = result;
    delay(100);
}
