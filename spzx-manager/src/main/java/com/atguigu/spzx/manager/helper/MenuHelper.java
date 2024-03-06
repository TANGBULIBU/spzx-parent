package com.atguigu.spzx.manager.helper;

import com.atguigu.spzx.model.entity.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

public class MenuHelper {

    /**
     * 使用递归方法建菜单
     * @param sysMenuList
     * @return
     */
    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {
        List<SysMenu> trees = new ArrayList<>();

        for (SysMenu sysMenu : sysMenuList) {
            //sysMenu的父节点是0 那就是头节点
            if (sysMenu.getParentId().longValue() == 0) {
                //对trees添加他的子节点 如果找到有孩子的话
                trees.add(findChildren(sysMenu,sysMenuList));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     * @param treeNodes
     * @return
     */
    public static SysMenu findChildren(SysMenu sysMenu, List<SysMenu> treeNodes) {
        sysMenu.setChildren(new ArrayList<SysMenu>());

        //将子节点递归并且给父节点
        for (SysMenu it : treeNodes) {
//            sysMenu的id和子节点的父id相等的话
            if(sysMenu.getId().longValue() == it.getParentId().longValue()) {
                //if (sysMenu.getChildren() == null) {
                //    sysMenu.setChildren(new ArrayList<>());
                //}
                //将ssyMenu的子节点定义未当前节点的值
//                sysMenu.getChildren().add(it); 存在两层的情况

                //此处方法是多个层级关系 递归反复调用子节点的子节点是否存在

                sysMenu.getChildren().add(findChildren(it,treeNodes));
            }
        }
        return sysMenu;
    }
}
