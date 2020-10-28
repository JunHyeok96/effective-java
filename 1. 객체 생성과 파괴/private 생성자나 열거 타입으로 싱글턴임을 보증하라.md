### **싱글톤 : 인스턴스를 오직 하나만 생성할 수 있는 클래스**

### **싱글톤 패턴을 왜 사용할까?**

인스턴스를 오직 하나만 생성하므로서 객체를 여러번 생성할 필요가 없고 객체를 공유할 수 있음.

스프링에서 관리하는 bean은 싱글톤 패턴으로 만들어져 의존성을 주입할때 동일한 객체를 사용함.

DBCP(DataBase Connection Pool)의 경우에도 DB와 애플리케이션을 연결하기위해 매번 객체를 생성하여 관리하는 것보다 최초의 생성된 객체를 재사용하면서 connection을 관리하는게 더 효율적임

### **싱글톤을 만드는 3가지 방법**

#### 첫번째, public static 맴버가 final로 선언된 경우

![image](https://user-images.githubusercontent.com/52908154/97434504-71b35680-1962-11eb-89c3-0001a7d60b45.png)

java파일 작성 -> java 컴파일러를 통해 class파일이 생성 -> 자바의 클래스 로더가 jvm에서 class파일을 로딩함

static은 객체가 생성되기전 클래스가 로딩되는 시점에서 맴버가 생성된다. 

장점.

public  필드 방식의 장점은 해당 클래스가 싱글턴임이 API에 명백히 드러난다.

간단하게 만들 수 있음.

단점.

리플렉션 API의 AccessibleObject.setAccessible을 사용하면 private생성자를 호출할 수있기 때문에 새로운 인스턴스를 만들수 있음 -> 이에 대한 해결책으로 2번째 객체가 생성될때 예외를 던지게하면됨

![image](https://user-images.githubusercontent.com/52908154/97434571-8859ad80-1962-11eb-9852-a9e7c4e08eaf.png)

리플렉션이란 ? 

구체적인 클래스 타입을 알지 못해도**컴파일된 바이트 코드**를 통해 해당 클래스의 메소드, 타입, 변수까지 접근가능한 자바 API. 리플렉션은 컴파일된 바이트 코드에 접근이 가능하기 때문에 static영역에도 접근가능하고 private로 선언된 부분까지 접근할 수 있다. 

![image](https://user-images.githubusercontent.com/52908154/97434732-cbb41c00-1962-11eb-8830-84cd3c86a945.png)

장점.

API를 바꾸지않고도 싱글턴이 아니게 변경할 수 있다. 유일한 인스턴스를 반환하던 팩터리 메서드가 호출하는 스레드별로 다른 인스턴스를 넘겨주게 할 수 있다.  

![image](https://user-images.githubusercontent.com/52908154/97434791-e1294600-1962-11eb-9b8d-2d5719d03761.png)

원한다면 정적팩터리를 제네릭 싱글턴 팩터리로 만들 수 있다는 점 ?

정적팩터리의 메서드 참조를 공급자(supplier)로 사용할 수 잇다는 점이다. 가령 Elvis::getInstance를 Supplier<Elvis>로 사용하는 식이다. 이런 장점이 굳이 필요하지 않으면 public필드 방식이 좋다. 리플렉션 예외는 그대로 적용

![image](https://user-images.githubusercontent.com/52908154/97434822-ebe3db00-1962-11eb-9b10-a328b4cf1472.png)

단점.

리플렉션 API의 AccessibleObject.setAccessible을 사용하면 private생성자를 호출 가능.

![image](https://user-images.githubusercontent.com/52908154/97434868-fef6ab00-1962-11eb-922a-d472a297da76.png)

위의 두가지 방식으로 직렬화를 하려면 Serialize를 구현한다고 선언하는것으로는 부족하다. 모든 인스턴트 필드를 일시적(transient)라고 선언하고 readResolve메서드를 제공해야한다. 이렇게 하지않으면 역직렬화때 매번 새로운 인스턴스가 만들어진다.  이를 예방하고 싶다면 readResolve를 추가한다.

```
//싱글턴임을 보장해주는 readResolve메서드
private Object readResolve(){
	return INSTANCE;
}
```

직렬화란?

자바시스템 내부의 객체 또는 데이터를 외부의 자바시스템에서도 사용할 수 있도록 Byte형태로 변환하는 기술(그 역은 역직렬화라고함)

직렬화 방법 (csv, Json, 프로토콜 버퍼 등등)이 존재하는데 자바에서 직렬화를 사용하는 이유는 ?

다른 직렬화 방법은 시스템에 상관없이 데이터를 교환하는데 사용됨. 자바 직렬화는 오직 자바 시스템간 교환에 사용됨.

그럼에도 자바 직렬화를 사용하는 이유? 자바 시스템에 최적화 되어 복잡한 데이터 구조도 쉽게 직렬화가 가능. 직렬화/역직렬화를 위해 다른 라이브러리가 필요없음. 역직렬화가 되면 바로 자바 객체로서 사용이 가능하다.

자세한 내용은 아래 링크를 참조

https://flowarc.tistory.com/entry/Java-%EA%B0%9D%EC%B2%B4-%EC%A7%81%EB%A0%AC%ED%99%94Serialization-%EC%99%80-%EC%97%AD%EC%A7%81%EB%A0%AC%ED%99%94Deserialization

https://woowabros.github.io/experience/2017/10/17/java-serialize.html

#### 세번째 방법 enum을 활용한 직렬화 방법

![image](https://user-images.githubusercontent.com/52908154/97434888-0918a980-1963-11eb-9c1f-e6b4e7f7d509.png)

enum의 특성 

1\. 상수들만 모아놓은 클래스로 생성자, 메서드를 가질 수 있다.

2\. private한 생성자를 가진다.

3\. enum타입은 고정된 상수들의 집합으로써, 런타임이 아닌 컴파일타임에 모든 값을 알고 있어야 합니다. 즉 다른 패키지나 클래스에서 enum 타입에 접근해서 동적으로 어떤 값을 정해줄 수 없음.

4\. 상속 불가

public 필드 방식과 비슷하지만, 더 간결하고 추가적인 노력없이 직렬화가 가능함. 또, 아주 복잡한 직렬화 상황이나 리플렉션 공격에도 제2의 인스턴스가 생기는 일을 완벽히 막아준다.  대부분 상항에서는 원소가 하나뿐인 열거 타입이 싱글턴을 만드는 가장 좋은 방법이라고 함. 제약 조건으로는 만들려는 싱글턴이 Enum외의 클래스를 상속해야한다면 이 방법은 사용할 수 없다.(열거 타입이 다른 인터페이스를 구현하도록 선언할 수는 있다.)
