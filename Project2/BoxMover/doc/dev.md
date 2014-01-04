BoxMover version2 开发文档
==========================

1.概述
------
在也不做死用HTML写文档了= =PJ1的文档写的蛋疼死了，Markdown多好卧槽！！

BoxMover version2是在v1的基础上修改来的，主要不同是程序结构发生了改变，并且有了GUI界面。本来PJ1希望可以复用大部分框架来直接重构PJ2的。。。不过后来发现自己还是Too Young Too Simple
总之最后只有Model部分是基本重用了的，原来构想的UI部分完全没有用武之地

2.程序结构
----------
一个十分伪的MVC结构，其实代码逻辑，UI，和数据模型没有完全分开。

代码的主要逻辑写在App类中，封装在GameEventAdapter中，后者是一个游戏事件监听器，用来处理所有的游戏逻辑。
Model方面和PJ1的Model大致上一样，只是增加了一个用户类User和一个排名名单RankList。
UI方面，整个游戏只有一个Frame，利用setContentPane方法用事先写好的不同的Panel来表示游戏的不同阶段和状态。

3.代码细节
----------
### 3.1 提要
由于PJ2在PJ1的基础上修改而来，很多和PJ1相同或者类似的地方就不再赘述。同时由于Deadline问题，很多和模块不会和PJ1那样详细说明了= =

### 3.2 utils类
好吧首先我承认这个类名实在是有点让人不爽。没什么，这个类本身就没有什么用，就是一个放一些辅助函数的地方
```java
class utils {
	public static JButton createButton(String path);
}
```

没错，这个类只有一个辅助函数createButton，根据给定的图标位置来创造一个无padding的solid-border按钮。

### 3.3 Env类
和PJ1一样这里也有一个Env类，效果是一样的，放一些meta data。具体是什么也和PJ1一样，不在这儿说明，具体内容用到的时候会说明，或者参考程序注释。

### 3.4 App类
程序的逻辑部分。
```java
class App {
	public class GameCommandAdapter implements GameCommandListner;

	void run();
	void setGameState(int);
}
```

实际上程序的逻辑存放在App的内部类GameCommandAdapter，这是一个GameCommandListener，定义在ctrl包中，实质上是一组回掉函数聚合，在特定的时候被调用。

run函数是程序的入口，运行这个函数使程序开始运行。

setGameState函数设置当前游戏的运行状态，参数是定义在App中的一组以GAME_STATE开头的常量。具体的可以参见代码注释。

### 3.5 ctrl包
刚刚介绍了代码的逻辑部分，那么接着这个思路讲下去好了。ctrl包定义的是GameEvent，只有两个类，一个是GameCommandEvent，一个是GameCommandListener。GameCommandEvent很简单，没有包含任何特殊的东西，我们只介绍一下GameCommandListener接口。

```java
interface GameCommandListener {
	public void onExit(GameCommandEvent e);
	public void onNewUser(GameCommandEvent e);
	public void onOldUser(GameCommandEvent e);

	public void onKeyUp(GameCommandEvent e);
	public void onKeyDown(GameCommandEvent e);
	public void onKeyLeft(GameCommandEvent e);
	public void onKeyRight(GameCommandEvent e);
	public void onBackStep(GameCommandEvent e);

	public void onLevelVictory(GameCommandEvent e);
	public void onLevelFailed(GameCommandEvent e);
	public void onBackToLevel(GameCommandEvent e);

	public void onShowAbout(GameCommandEvent e);
	public void onShowHelp(GameCommandEvent e);
	public void onShowRank(GameCommandEvent e);

	public void onChooseLevel(GameCommandEvent e);
	public void onRestartLevel(GameCommandEvent e);
	public void onSaveLevel(GameCommandEvent e);
}
```
大多数回调函数的意义通过其函数名字就可以看出来，不再介绍。只介绍一下onBackToLevel，这个函数在我们离开游戏界面，前去察看一个如Author，Instruction之类的面板之后，按下Back Button的时候被调用，用来从非游戏状态返回到游戏状态。

### 3.6 exp包
略过了- -实在没什么好讲的，就是那几个异常。

### 3.7 ui包
UI界面全部定义在这儿了。

#### 3.7.1 Frame
首先所有UI的容器都是这个东西：一个自定义的Frame
```java
class Frame {
	public Frame(GameCommandListner);
	public void setGameState(GenericPanel, GameCommandListner, JMenuBar);
}
```
setGameState函数接受三个参数，表示当前的游戏面板，游戏监听器，和目录。

#### 3.7.2 游戏面板
所有的游戏面板都是一个GenericPanel。GenericPanel继承于JPanel，没有什么特殊的地方，只是多了一个destroy方法，在面板被切换的时候调用。

有一类特殊的面板，也就是非游戏面板，同时也是非开始界面的面板，用来显示信息，这类面板有一个继承于GenericPanel的抽象，叫做InfoPanel。
HelpPanel，LevelVictoryPanel，RankPanel，AuthorPanel都是继承于这个Panel的。
InfoPanel抽象了这部分Panel的共同点

```java
abstract class InfoPanel extends GenericPanel {
	private ImageIcon bg;
	protected JButton bt;
	
	public InfoPanel(String bgPath, String btPath, boolean flagExit);
}
```

这类Panel都有一个背景图片bg和一个退出按钮bt。通过构造函数的bgPath指定背景图片位置，btPath指定button的图片位置。第三个参数flagExit表示是否使用默认的退出按钮事件，如果使用的话，点击bt将会触发backToLevel回调。

之后是StartPanel，表示的是游戏的开始界面。

然后是GamePanel，表示的是游戏界面。

#### 3.7.3 菜单栏
MenuBar继承于JMenuBar，我们将所有的目录的构造过程都放在了MenuBar的构造函数中完成。MenuBar构造信息的元数据存放在MENU_ARCH数组中，具体参见代码。

### 3.8 widget包
也就是传说中的Model部分了。大部分内容和PJ1一样的，这部分内容不再赘述，详细的话参考PJ1的开发文档，非常的详细。

相对于PJ1的Widget部分，PJ2中的哦了两个东西，首先是表示用户的User类
```java
class User implements Serializable {
	private User();

	public BMMap getMap();
	public String getName();

	public void save();

	public static User newUser(String name);
	public static User getUser(String name);

	public void incScore(int);

	public int getHighLv();
	public int incHighLv();
}

```
User类不能直接创建（所以构造函数是private的），只能通过两个静态方法，创建一个心用户，或者说得到一个已经存在的用户。save方法将当前用户的信息保存在文件中。highLv属性表示当前用户可以玩的最大关卡数，incHighLv将这个最大关卡数增加。

另外一个新增加的Model是RankList，存放的是排名信息。关于RankList唯一需要说明的是，这是一个Singleton。

4. 总结
-------
PJ2写完了，对Java的GUI有了一定的了解。感觉Java的GUI很诡异，用起来很不顺手，主要是事件驱动这一块，和主流的GUI比如gtk，qt等等，模型不一样啊！！！
不过都无所谓了，总之是写完了23333333。
