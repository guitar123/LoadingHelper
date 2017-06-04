/**
 * Copyright 2015 Pengyuan-Jiang
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Author：Ybao on 2015/11/3 ‏‎17:44
 * <p>
 * QQ: 392579823
 * <p>
 * Email：392579823@qq.com
 */
package zz.itcast.cn.loading.pullrefreshview.support.impl;


import zz.itcast.cn.loading.pullrefreshview.layout.PullRefreshLayout;

/**
 * 可加载更多的接口回调
 */
public interface Loadable {

    void setPullRefreshLayout(PullRefreshLayout refreshLayout);

    /**
     * 在底部上拉加载更多的时候y坐标变化的回调方法
     * @param y 移动到y坐标值
     * @return false FlingLayout会越界运动，true FlingLayout会固定到顶部或者底部
     */
    boolean onScroll(float y);

    /**
     * 状态改变
     *
     * @param state SCROLL_STATE_IDLE,SCROLL_STATE_TOUCH_SCROLL,SCROLL_STATE_FLING
     */
    void onScrollChange(int state);

    boolean onStartFling(float offsetTop);

    void startLoad();

    void stopLoad();
}
