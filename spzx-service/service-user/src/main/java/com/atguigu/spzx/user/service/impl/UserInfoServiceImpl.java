package com.atguigu.spzx.user.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.common.utils.StringUtils;
import com.atguigu.spzx.common.exception.GuiguException;
import com.atguigu.spzx.model.dto.h5.UserLoginDto;
import com.atguigu.spzx.model.dto.h5.UserRegisterDto;
import com.atguigu.spzx.model.entity.user.UserInfo;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.h5.UserInfoVo;
import com.atguigu.spzx.user.mapper.UserInfoMapper;
import com.atguigu.spzx.user.service.UserInfoService;
import com.atguigu.spzx.utils.AuthContextUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
* @author 77943
* @description 针对表【user_info(会员表)】的数据库操作Service实现
* @createDate 2024-03-20 14:43:29
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RedisTemplate<String , String> redisTemplate;

//    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(UserRegisterDto userRegisterDto) {
// 获取数据
        String username = userRegisterDto.getUsername();
        String password = userRegisterDto.getPassword();
        String nickName = userRegisterDto.getNickName();
        String code = userRegisterDto.getCode();

        //校验参数
        if(StringUtils.isEmpty(username) ||
                StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(nickName) ||
                StringUtils.isEmpty(code)) {
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }

        //校验验证码
        //从redis中获取验证码
        String codeValueRedis = redisTemplate.opsForValue().get("phone:code:" + username);
        /*if(!codeValueRedis.equals(code)){
            会出现空指针异常
        }*/
        if(!code.equals(codeValueRedis)){
            throw new GuiguException(ResultCodeEnum.VALIDATECODE_ERROR);
        }

        //校验用户名是否存在
        LambdaQueryWrapper<UserInfo> userInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userInfoLambdaQueryWrapper.eq(UserInfo::getUsername, username);
        UserInfo userInfo = userInfoMapper.selectOne(userInfoLambdaQueryWrapper);
        if(userInfo != null){
            throw new GuiguException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }

        //保存用户信息
        userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setNickName(nickName);
        userInfo.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        userInfo.setAvatar("https://q2.itc.cn/q_70/images03/20240312/519714591a8241b884f21e37b54d4319.jpeg");
        userInfo.setMemo("这个人很懒，什么都没有留下");
        userInfo.setPhone(username);
        userInfo.setSex(0);
        userInfo.setStatus(1); //正常
        userInfoMapper.save(userInfo);

        // 删除Redis中的数据
        redisTemplate.delete("phone:code:" + username) ;
    }

//   登录
    @Override
    public String login(UserLoginDto userLoginDto, String ipAddr) {
        //根据用户名查询用户数据
        UserInfo byUsername = baseMapper.getByUsername(userLoginDto.getUsername());
        //如果用户名为空 就返回登录失败
        if(byUsername == null){
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
        }

        //通过UserName获取它的password 与用户输入得值是否相同
        if (!byUsername.getPassword().equals(DigestUtils.md5DigestAsHex(userLoginDto.getPassword().getBytes()))){
            throw new GuiguException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        //是否被禁用
        if(byUsername.getStatus() == 0){
            throw new GuiguException(ResultCodeEnum.ACCOUNT_STOP);
        }

        //生成token
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        //将token存入redis 将信息以json存入
        redisTemplate.opsForValue().set("user:login:"+token, JSON.toJSONString(byUsername),30, TimeUnit.DAYS);

        return token;
    }

    /*
    获取当前用户信息 nickname avatar
     */
    @Override
    public UserInfoVo getCurrentUserInfo(String token) {
        /*
        使用了ThreadLocal后 将redis中token用户信息直接存入ThreadLocal中
        //从redis中获取相关用户得token
        String userInfoJson = redisTemplate.opsForValue().get("user:login:"+token);
        //如果没有token 就返回登录失败
        if(StringUtils.isEmpty(userInfoJson)){
            throw new GuiguException(ResultCodeEnum.LOGIN_AUTH);
        }

        //将用户信息从json转为UserInfo对象
        UserInfo userInfo = JSON.parseObject(userInfoJson, UserInfo.class);
         */

        //util工具类使用从ThreadLocal中获取相关信息
        UserInfo userInfo = AuthContextUtil.getUserInfo();

        //封装信息 到vo类中
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setAvatar(userInfo.getAvatar());
        userInfoVo.setNickName(userInfo.getNickName());
        return userInfoVo;
    }
}




