# GenieEffectApplication
Genie effect Android lib

[toc]

Currently support animation to right/bottom/top sides. Left side is remained to be done.

![image](https://github.com/gumuxiansheng/GenieEffectApplication/blob/master/genie_effect.gif?raw=true)

## Usage

The sample app shows the way to use this genie effect library.
```java
private AnimSurface mAnimSurface;
if (mAnimSurface == null) {
    mAnimSurface = new AnimSurface(GenieSampleActivity.this);
    mAnimSurface.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
}
AnimSurfaceUtil.startAnimation(GenieSampleActivity.this, mAnimSurface, image, mReverse);
mReverse = !mReverse;
```

## Thoughts

The main thoughts are like below:

The final path:

![pathIllustrate1](https://github.com/gumuxiansheng/GenieEffectApplication/blob/master/pathIllustrate1.png?raw=true)

The path on half way:

![pathIllustrate2](https://github.com/gumuxiansheng/GenieEffectApplication/blob/master/pathIllustrate2.png?raw=true)
