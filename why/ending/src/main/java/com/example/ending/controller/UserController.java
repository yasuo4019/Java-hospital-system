package com.example.ending.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ending.common.Constants;
import com.example.ending.common.Result;
import com.example.ending.controller.dto.UserDTO;
import com.example.ending.controller.dto.UserPasswordDTO;
import com.example.ending.entity.LoginAttempt;
import com.example.ending.entity.RegistrationAttempt;
import com.example.ending.entity.User;
import com.example.ending.exception.ServiceException;
import com.example.ending.mapper.UserMapper;
import com.example.ending.repository.LoginAttemptRepository;
import com.example.ending.repository.RegistrationAttemptRepository;
import com.example.ending.service.IUserService;
import com.example.ending.utils.TokenUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest; // 确保导入了 HttpServletRequest
import cn.hutool.crypto.digest.DigestUtil;


import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 王宏远
 * @since 2023-05-06
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private LoginAttemptRepository loginAttemptRepository; // 注入 LoginAttemptRepository

    @Resource
    private RegistrationAttemptRepository RegistrationAttemptRepository; // 注入 RegistrationAttemptRepository



    //登录接口
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO, HttpServletRequest request) { // 添加 HttpServletRequest 作为参数
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        // 验证输入是否包含特殊字符
        if (!isValidInput(username) || !isValidInput(password)) {
            return Result.error(Constants.CODE_400, "Username or password should not contain special characters");
        }
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400,"Parameter error");
        }
        // 检查登录尝试次数是否过多
        if (isLoginAttemptExceeded(username)) {
            return Result.error(Constants.CODE_429, "The login has been temporarily locked due to too many attempts. Please try again later.");
        }
        try {
            UserDTO dto = userService.login(userDTO);
            // 记录成功的登录尝试
            recordLoginAttempt(username, request.getRemoteAddr(), true, null); // 现在可以使用 request 对象
            return Result.success(dto);
        } catch (Exception e) {
            // 记录失败的登录尝试
            recordLoginAttempt(username, request.getRemoteAddr(), false, e.getMessage());
            return Result.error(Constants.CODE_401, "Login failed: " + e.getMessage());
        }
    }

    private boolean isValidInput(String input) {
        // 此正则表达式用于检查输入是否包含特殊字符
        Pattern specialChars = Pattern.compile("[!@#$%^&*(),.?\":{}|<>=-]");
        return !specialChars.matcher(input).find();
    }

    private void recordLoginAttempt(String username, String ip, boolean success, String errorMessage) {
        // 创建一个新的 LoginAttempt 实体并设置值
        LoginAttempt attempt = new LoginAttempt();
        attempt.setUsername(username);
        attempt.setIp(ip);
        // 将布尔型的success转换为int类型
        attempt.setSuccess(success ? 1 : 0); // 如果success为true，则存储1，否则存储0
        attempt.setErrorMessage(errorMessage);
        attempt.setAttemptTime(new Date()); // 设置当前时间为尝试时间

        // 将这个实体保存到数据库
        loginAttemptRepository.save(attempt);
    }
    private boolean isLoginAttemptExceeded(String username) {
        Date oneMinuteAgo = new Date(System.currentTimeMillis() - 60000); // 一分钟前的时间
        // 检查过去一分钟内的所有登录尝试次数
        int totalAttempts = loginAttemptRepository.countByAttemptTimeAfter(oneMinuteAgo);
        if (totalAttempts >= 3) {
            return true; // 如果一分钟内尝试超过3次，返回 true
        }
        // 检查特定用户的错误密码尝试次数
        int userFailedAttempts = loginAttemptRepository.countByUsernameAndSuccessAndAttemptTimeAfter(username, 0, oneMinuteAgo);
        if (userFailedAttempts >= 3) {
            return true; // 如果用户一分钟内有3次失败尝试，返回 true
        }
        return false;
    }


    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "Parameter error");
        }
        // 检查输入是否包含特殊字符
        if (containsSpecialCharacters(username) || containsSpecialCharacters(password)) {
            return Result.error(Constants.CODE_400, "Username or password should not contain special characters");
        }
        if (isRegistrationAttemptExceeded()) {
            return Result.error(Constants.CODE_429, "Registration has been temporarily locked due to too many registration attempts. Please try again later.");
        }
        try {
            userDTO.setRole("ROLE_USER");
            User dto = userService.register(userDTO);
            recordRegistrationAttempt(username, request.getRemoteAddr(), true, null);
            return Result.success(dto);
        } catch (Exception e) {
            recordRegistrationAttempt(username, request.getRemoteAddr(), false, e.getMessage());
            return Result.error(Constants.CODE_401, "registration failed: " + e.getMessage());
        }
    }

    private boolean containsSpecialCharacters(String input) {
        // 此正则表达式用于检查输入是否包含特殊字符
        Pattern specialChars = Pattern.compile("[!@#$%^&*(),.?\":{}|<>=-]");
        return !specialChars.matcher(input).find();
    }

    private void recordRegistrationAttempt(String username, String ip, boolean success, String errorMessage) {
        RegistrationAttempt attempt = new RegistrationAttempt();
        attempt.setUsername(username);
        attempt.setIp(ip);
        // 将布尔型的success转换为int类型
        attempt.setSuccess(success ? 1 : 0); // 如果success为true，则存储1，否则存储0
        attempt.setErrorMessage(errorMessage);
        attempt.setAttemptTime(new Date()); // 设置当前时间为尝试时间

        RegistrationAttemptRepository.save(attempt); // 假设您有一个类似的repository用于保存注册尝试
    }
    private boolean isRegistrationAttemptExceeded() {
        Date oneMinuteAgo = new Date(System.currentTimeMillis() - 60000); // 一分钟前的时间
        Date oneHourAgo = new Date(System.currentTimeMillis() - 3600000); // 一小时前的时间

        int attemptsLastMinute = RegistrationAttemptRepository.countByAttemptTimeAfter(oneMinuteAgo);
        if (attemptsLastMinute >= 3) {
            return true;
        }
        int attemptsLastHour = RegistrationAttemptRepository.countByAttemptTimeAfter(oneHourAgo);
        if (attemptsLastHour >= 20) {
            return true;
        }
        return false;
    }

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody User user) {
        //
        if (user.getId() == null && user.getPassword() == null) {  // 新增用户默认密码
            // 首次对"123"进行SHA256加密
            String encryptedOnce = DigestUtil.sha256Hex("1234567890");
            // 再次对加密结果进行SHA256加密
            String encryptedTwice = DigestUtil.sha256Hex(encryptedOnce);
            // 设置用户密码为两次加密后的结果
            user.setPassword(encryptedTwice);
        }
        return Result.success(userService.saveOrUpdate(user));
    }

    //23
    /**
     * 修改密码
     * @param userPasswordDTO
     * @return
     */
    @PostMapping("/password")
    public Result password(@RequestBody UserPasswordDTO userPasswordDTO) {
        // 对原密码和新密码进行第二次加密
        userPasswordDTO.setPassword(DigestUtil.sha256Hex(userPasswordDTO.getPassword()));
        userPasswordDTO.setNewPassword(DigestUtil.sha256Hex(userPasswordDTO.getNewPassword()));
        // 获取现有用户信息
        User existingUser = userMapper.selectByUsername(userPasswordDTO.getUsername());
        // 验证原密码
        if (existingUser != null && userPasswordDTO.getPassword().equals(existingUser.getPassword())) {
            // 更新数据库中的密码
            int update = userMapper.updatePassword(userPasswordDTO); // 确保这个方法更新了密码字段
            // 检查更新操作是否成功
            if (update < 1) {
                throw new ServiceException(Constants.CODE_600, "Password update failed");
            }
            return Result.success();
        } else {
            throw new ServiceException(Constants.CODE_600, "The current password is incorrect");
        }
    }



    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(userService.removeById(id));
    }


    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(userService.removeByIds(ids));
    }


    @GetMapping
    public Result findAll() {
        return Result.success(userService.list());
    }


    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }


    @GetMapping("/username/{username}")
    public Result findOne(@PathVariable String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return Result.success(userService.getOne(queryWrapper));
    }


    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam(defaultValue = "") String username,
                               @RequestParam(defaultValue = "") String email,
                               @RequestParam(defaultValue = "") String address) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(username)) {
            queryWrapper.like("username", username);
        }
        if (!"".equals(email)) {
            queryWrapper.like("email", email);
        }
        if (!"".equals(address)) {
            queryWrapper.like("address", address);
        }

        //获取当前用户信息
        User currentUser = TokenUtils.getCurrentUser();
        System.out.println("获取当前用户信息============================" + currentUser.getNickname());

        return Result.success(userService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
     * 导出接口
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<User> list = userService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("nickname", "昵称");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("createtime", "创建时间");
        writer.addHeaderAlias("avatarUrl", "头像");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }

    /**
     * excel 导入
     *
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream); // 通过javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        //List<User> list = reader.readAll(User.class);

        //忽略表头的中文，第二种方式,直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<User> users = CollUtil.newArrayList();
        for (List<Object> row : list) {
            User user = new User();
            user.setUsername(row.get(0).toString());
            user.setPassword(row.get(1).toString());
            user.setNickname(row.get(2).toString());
            user.setEmail(row.get(3).toString());
            user.setPhone(row.get(4).toString());
            user.setAddress(row.get(5).toString());
            user.setAvatarUrl(row.get(6).toString());
            users.add(user);
        }
        userService.saveBatch(users);
        return Result.success(true);
    }
}