# GenieEffectApplication
Genie effect Android lib

Currently support animation to right/bottom/top sides. Left side is remained to be done.

![image](https://github.com/gumuxiansheng/GenieEffectApplication/blob/master/genie_effect.gif?raw=true)
*图片来源：Dean, J., & Ghemawat, S. (2008). MapReduce: Simplified data processing on large clusters. Communications of the ACM, 51(1), 107–113. https://doi.org/10.1145/1327452.1327492*

## Usage

The sample app shows the way to use this genie effect library.
```java
private AnimSurface mAnimSurface; // declare the surface to show the animation, usually capture all the screen.
if (mAnimSurface == null) {
    mAnimSurface = new AnimSurface(GenieSampleActivity.this);
    mAnimSurface.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
}
// By passing the view to be animated to the util's function to start the animation on AnimSurface.
AnimSurfaceUtil.startAnimation(GenieSampleActivity.this, mAnimSurface, image, mReverse); // image is the view to be animated.
mReverse = !mReverse;
```
where image is the view to be animated.

## Thoughts

The path in animation is very important here, the key in this lib is to calculate the right shape at time T then apply it with mesh.

The illustration is like below:

The final path:

![pathIllustrate1](https://github.com/gumuxiansheng/GenieEffectApplication/blob/master/pathIllustrate1.png?raw=true)

The path on half way:

![pathIllustrate2](https://github.com/gumuxiansheng/GenieEffectApplication/blob/master/pathIllustrate2.png?raw=true)
