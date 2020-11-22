## **\+ 연산으로 계산한 경우**

10만개의 "abcd"를  붙였다.

```java
public static void main(String[] args){
    long startTime = System.currentTimeMillis();
    String result = "";
    for(int i=0; i<100000; i++){
      result += "abcd";
    }
    long endTime = System.currentTimeMillis();
    System.out.println("걸린 시간 : " + (float)(endTime-startTime)/1000 + "초");
  }
```

![image](https://user-images.githubusercontent.com/52908154/99896574-30019b80-2cd5-11eb-89a9-ccf42a727447.png)

**문자열 연결 연산자로 문자열n개를 잇는 시간은 n^2에 비례한다.** 문자열은 immutable해서 두 문자열을 연결할 경우 양쪽 내용을 모두 복사해야한다.

## **StringBuilder를 사용한 경우**

10만개의 "abcd"를  붙였다.

```java
 public static void main(String[] args){
    long startTime = System.currentTimeMillis();
    StringBuilder builder = new StringBuilder(4 * 100000);
    for(int i=0; i<100000; i++){
      builder.append("abcd");
    }
    String result = builder.toString();
    long endTime = System.currentTimeMillis();
    System.out.println("걸린 시간 : " + (float)(endTime-startTime)/1000 + "초");
  }
```

![image](https://user-images.githubusercontent.com/52908154/99896592-4dcf0080-2cd5-11eb-9ebe-118d5912d4a1.png)

용량 설정을 입력 수와 달리하고  500만개의 "abcd"를  붙였다.

```java
public static void main(String[] args){
   
    ..
    
    StringBuilder builder = new StringBuilder(4 * 100000);
    for(int i=0; i<5000000; i++){
      builder.append("abcd");
    }
   
    ..
  }
```

![image](https://user-images.githubusercontent.com/52908154/99896598-5c1d1c80-2cd5-11eb-8104-14b3c091bfbb.png)

용량 설정을 입력 수와 맞추고  500만개의 "abcd"를  붙였다.

```java
public static void main(String[] args){
    ..
    
    StringBuilder builder = new StringBuilder(4 * 5000000);
    for(int i=0; i<5000000; i++){
      builder.append("abcd");
    }
    
    ..
  }
```

![image](https://user-images.githubusercontent.com/52908154/99896601-68a17500-2cd5-11eb-82b8-d1554cc45ef0.png)

append과정에서 확인하고 용량이 부족하면 용량을 입력받은 String의 길이만큼 더 추가한다. 매번 이 작업이 이뤄지기 때문에 초기 용량 값 설정이 속도에 영향을 준다.

![image](https://user-images.githubusercontent.com/52908154/99896611-7f47cc00-2cd5-11eb-8259-7da0fe26d30c.png)

## **concat을 사용한 경우**

10만개의 "abcd"를  붙였다.

```java
public static void main(String[] args){
    
    ..
    
    String result = "";
    for(int i=0; i<100000; i++){
      result = result.concat("abcd");
    }
    
    ..
  }
```

![image](https://user-images.githubusercontent.com/52908154/99896617-8ec71500-2cd5-11eb-9aad-7bcacd1a05fe.png)

concat과 StringBuilder의 차이점은 **concat은 매번 새로운 String 객체를 만들고** **StringBuilder는** append로 char array를 만들었다가 **toString메서드 호출시 String객체를 만든다.**

## **StringBuffer를 사용한 경우**

10만개의 "abcd"를  붙였다.

```java
public static void main(String[] args){
	..
    
	StringBuffer sb = new StringBuffer(4* 100000);
    for(int i=0; i<100000; i++){
      sb.append("abcd");
    }
    String result = sb.toString();
    
    ..
  }
```

![image](https://user-images.githubusercontent.com/52908154/99896618-94bcf600-2cd5-11eb-9769-c150583b4f85.png)

용량 설정을 입력 수와 달리하고  500만개의 "abcd"를  붙였다.

```java
public static void main(String[] args){
	..
    
    StringBuffer sb = new StringBuffer(4* 100000);
    for(int i=0; i<5000000; i++){
      sb.append("abcd");
    }
    String result = sb.toString();
    
    ..
  }
```

![image](https://user-images.githubusercontent.com/52908154/99896628-a30b1200-2cd5-11eb-8f60-a6973945e41e.png)

용량 설정을 입력 수 같게하고 500만개의 "abcd"를  붙였다.

```java
public static void main(String[] args){
	..
    
    StringBuffer sb = new StringBuffer(4* 5000000);
    for(int i=0; i<5000000; i++){
      sb.append("abcd");
    }
    String result = sb.toString();
    
    ..
  }
```

![image](https://user-images.githubusercontent.com/52908154/99896633-aacab680-2cd5-11eb-9c34-af5058faf7b7.png)

![image](https://user-images.githubusercontent.com/52908154/99896635-b28a5b00-2cd5-11eb-9f06-2bf7657c82fa.png)

StringBuffer의 append도 타고가보면 StringBuilder와 같은 메서드를 사용한다.

비교 결과 : StringBuilder > StringBuffer > concat >> +연산

실제 벤치 마크를 비교해보자.

![image](https://user-images.githubusercontent.com/52908154/99896638-ba49ff80-2cd5-11eb-9aa9-298d8f0ba427.png)

StringBuilder와 StringBuffer가 유사하고 그다음으로 concat과 +연산 순서였다.

### **StringBuilder와 StringBuffer중 어떤걸 써야하나?**

둘다 append시 같은 메서드를 사용하고 있었다. 그렇다면 어떤 차이가 있을까?

![image](https://user-images.githubusercontent.com/52908154/99896642-c6ce5800-2cd5-11eb-8be0-73cbe9ab5309.png)

둘의 차이는 synchronized에 있다. thread safe하게 보장하는지 아닌지가 다른 것이다.

![image](https://user-images.githubusercontent.com/52908154/99896645-d2ba1a00-2cd5-11eb-9ebd-7a4c98e0d0bc.png)

synchronized키워드르 사용해서 동기화를 하게되면 block과unblock을처리하게되는데이런처리로 인해 프로그램 성능이 저하될 수 있다.

단일 스레드 환경에서는 StringBuilder, 멀티 스레드 환경에서는 StringBuffer가 적합하다.
