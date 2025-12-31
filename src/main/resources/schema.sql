-- 个人物品分享系统数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS item_share DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE item_share;

-- 用户表
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    email VARCHAR(100) UNIQUE NOT NULL COMMENT '邮箱',
    password VARCHAR(255) NOT NULL COMMENT '密码(加密)',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像URL',
    bio TEXT COMMENT '个人简介',
    phone VARCHAR(20) COMMENT '手机号',
    location VARCHAR(100) COMMENT '所在地区',
    status TINYINT DEFAULT 1 COMMENT '状态:0-禁用,1-正常',
    email_verified TINYINT DEFAULT 0 COMMENT '邮箱是否验证:0-未验证,1-已验证',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    last_login_at TIMESTAMP NULL COMMENT '最后登录时间'
) COMMENT='用户表';

-- 分类表
CREATE TABLE categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    description TEXT COMMENT '分类描述',
    icon VARCHAR(100) COMMENT '分类图标',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID,0表示顶级分类',
    sort_order INT DEFAULT 0 COMMENT '排序权重',
    status TINYINT DEFAULT 1 COMMENT '状态:0-禁用,1-正常',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='物品分类表';

-- 物品表
CREATE TABLE items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '物品ID',
    title VARCHAR(200) NOT NULL COMMENT '物品标题',
    description TEXT COMMENT '物品描述',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    user_id BIGINT NOT NULL COMMENT '发布用户ID',
    condition_type TINYINT NOT NULL COMMENT '物品状态:1-全新,2-几乎全新,3-轻微使用痕迹,4-明显使用痕迹,5-需要维修',
    price DECIMAL(10,2) DEFAULT 0.00 COMMENT '价格,0表示免费',
    original_price DECIMAL(10,2) DEFAULT 0.00 COMMENT '原价',
    is_free TINYINT DEFAULT 0 COMMENT '是否免费:0-否,1-是',
    status TINYINT DEFAULT 1 COMMENT '状态:0-已删除,1-待审核,2-审核通过,3-审核拒绝,4-已下架',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    favorite_count INT DEFAULT 0 COMMENT '收藏次数',
    contact_method VARCHAR(100) COMMENT '联系方式',
    location VARCHAR(100) COMMENT '物品位置',
    latitude DECIMAL(10,8) COMMENT '纬度',
    longitude DECIMAL(11,8) COMMENT '经度',
    tags VARCHAR(500) COMMENT '标签,用逗号分隔',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    reviewed_at TIMESTAMP NULL COMMENT '审核时间',
    reviewed_by BIGINT NULL COMMENT '审核人ID',
    INDEX idx_category_user (category_id, user_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    INDEX idx_location (latitude, longitude),
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
) COMMENT='物品表';

-- 图片表
CREATE TABLE images (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '图片ID',
    item_id BIGINT NOT NULL COMMENT '物品ID',
    user_id BIGINT NOT NULL COMMENT '上传用户ID',
    original_name VARCHAR(255) COMMENT '原始文件名',
    file_name VARCHAR(255) NOT NULL COMMENT '存储文件名',
    file_path VARCHAR(500) NOT NULL COMMENT '文件路径',
    file_size INT NOT NULL COMMENT '文件大小(字节)',
    mime_type VARCHAR(100) NOT NULL COMMENT 'MIME类型',
    width INT COMMENT '图片宽度',
    height INT COMMENT '图片高度',
    sort_order INT DEFAULT 0 COMMENT '排序权重',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_item_id (item_id),
    INDEX idx_user_id (user_id),
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id)
) COMMENT='图片表';

-- 收藏表
CREATE TABLE favorites (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏ID',
    user_id BIGINT NOT NULL COMMENT '收藏用户ID',
    item_id BIGINT NOT NULL COMMENT '物品ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_item (user_id, item_id),
    INDEX idx_user_id (user_id),
    INDEX idx_item_id (item_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
) COMMENT='收藏表';

-- 关注表
CREATE TABLE follows (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关注ID',
    follower_id BIGINT NOT NULL COMMENT '关注者ID',
    following_id BIGINT NOT NULL COMMENT '被关注者ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_follower_following (follower_id, following_id),
    INDEX idx_follower_id (follower_id),
    INDEX idx_following_id (following_id),
    FOREIGN KEY (follower_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (following_id) REFERENCES users(id) ON DELETE CASCADE
) COMMENT='关注表';

-- 消息表
CREATE TABLE messages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '消息ID',
    sender_id BIGINT NOT NULL COMMENT '发送者ID',
    receiver_id BIGINT NOT NULL COMMENT '接收者ID',
    item_id BIGINT COMMENT '相关物品ID',
    type TINYINT NOT NULL COMMENT '消息类型:1-私信,2-系统通知,3-物品相关',
    title VARCHAR(200) COMMENT '消息标题',
    content TEXT NOT NULL COMMENT '消息内容',
    is_read TINYINT DEFAULT 0 COMMENT '是否已读:0-未读,1-已读',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    read_at TIMESTAMP NULL COMMENT '阅读时间',
    INDEX idx_receiver_read (receiver_id, is_read),
    INDEX idx_item_id (item_id),
    FOREIGN KEY (sender_id) REFERENCES users(id),
    FOREIGN KEY (receiver_id) REFERENCES users(id),
    FOREIGN KEY (item_id) REFERENCES items(id)
) COMMENT='消息表';

-- 浏览历史表
CREATE TABLE view_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '浏览ID',
    user_id BIGINT COMMENT '浏览用户ID,游客为NULL',
    item_id BIGINT NOT NULL COMMENT '物品ID',
    ip_address VARCHAR(45) COMMENT 'IP地址',
    user_agent TEXT COMMENT '用户代理',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_item (user_id, item_id),
    INDEX idx_item_created (item_id, created_at),
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
) COMMENT='浏览历史表';

-- 系统配置表
CREATE TABLE system_configs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '配置ID',
    config_key VARCHAR(100) UNIQUE NOT NULL COMMENT '配置键',
    config_value TEXT COMMENT '配置值',
    description VARCHAR(255) COMMENT '配置描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='系统配置表';

-- 操作日志表
CREATE TABLE operation_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    user_id BIGINT COMMENT '操作用户ID',
    operation VARCHAR(100) NOT NULL COMMENT '操作类型',
    target_type VARCHAR(50) NOT NULL COMMENT '目标类型',
    target_id BIGINT NOT NULL COMMENT '目标ID',
    description TEXT COMMENT '操作描述',
    ip_address VARCHAR(45) COMMENT 'IP地址',
    user_agent TEXT COMMENT '用户代理',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_target (target_type, target_id),
    INDEX idx_created_at (created_at),
    FOREIGN KEY (user_id) REFERENCES users(id)
) COMMENT='操作日志表';

-- 初始化分类数据
INSERT INTO categories (name, description, icon, sort_order) VALUES
('电子产品', '手机、电脑、相机等电子设备', 'electronics', 1),
('服装配饰', '衣服、鞋子、包包、首饰等', 'fashion', 2),
('家居用品', '家具、厨具、装饰品等', 'home', 3),
('书籍文具', '图书、文具、办公用品等', 'books', 4),
('运动户外', '运动器材、户外装备等', 'sports', 5),
('母婴用品', '婴幼儿用品、玩具等', 'baby', 6),
('其他', '其他类型物品', 'other', 99);

-- 初始化系统配置数据
INSERT INTO system_configs (config_key, config_value, description) VALUES
('site_name', '个人物品分享系统', '网站名称'),
('site_description', '让闲置物品发挥更大价值', '网站描述'),
('upload_max_size', '5242880', '文件上传最大大小(字节)'),
('items_per_page', '20', '每页显示物品数量'),
('auto_audit', '1', '是否自动审核:0-否,1-是');

-- 创建默认管理员用户 (密码: admin123)
INSERT INTO users (username, email, password, nickname, status, email_verified) VALUES
('admin', 'admin@itemshare.com', '$2a$10$7JB720yubVSOfvXq5qQ.3u8Z0k5Y5V7G5H6G6G6H6G6H6G6H6G6', '系统管理员', 1, 1);
