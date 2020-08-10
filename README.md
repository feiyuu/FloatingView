# FloatingView
博客：https://blog.csdn.net/m0_38058826/article/details/103993392

 [![](https://jitpack.io/v/feiyuu/FloatingView.svg)](https://jitpack.io/#feiyuu/FloatingView)   
 高性能，任何机型都无需适配 
支持加载圆形图片，gif动图，圆形阴影，全局显示，记录位置，平滑的吸附贴边，支持播放raw下文件 
无需权限，小米魅族华为不需适配机型，代码少，性能比悬浮窗高很多。 
1.0.4：优化了整体
1.0.6：去掉了所有依赖，防止引入冲突

<p align="center">
	<img src="https://github.com/feiyuu/FloatingView/blob/master/untitled.gif" alt="图裂请去博客看"  width="280" height="498">
	<p align="center">
		<em>图片示例</em>
	</p>
</p>

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

	dependencies {
	        implementation 'com.github.feiyuu:FloatingView:1.0.6'
	}
写的不好，没有考虑封装，要用的哥们下源码结合自己项目封装下吧
