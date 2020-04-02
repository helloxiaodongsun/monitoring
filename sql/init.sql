
drop table sys_menu;
create table sys_menu (
  menu_id           varchar2(10) PRIMARY KEY NOT NULL,
  menu_name         varchar2(50) not null,
  parent_id         varchar2(10),
  order_num         integer,
  url               varchar2(200) default '#',
  target            varchar2(20),
  menu_type         varchar2(1),
  visible           varchar2(1) default '0',
  perms             varchar2(100) default null,
  icon              varchar2(100) default '#',
  create_by         varchar2(64),
  create_time       date,
  update_by         varchar2(64),
  update_time       date,
  remark            varchar2(500)
);

COMMENT ON TABLE sys_menu IS '菜单表';
COMMENT ON COLUMN sys_menu.menu_id      IS'菜单ID';
COMMENT ON COLUMN sys_menu.menu_name    IS'菜单名称';
COMMENT ON COLUMN sys_menu.parent_id    IS'父菜单ID';
COMMENT ON COLUMN sys_menu.order_num    IS'显示顺序';
COMMENT ON COLUMN sys_menu.url          IS'请求地址';
COMMENT ON COLUMN sys_menu.target       IS'打开方式（menuItem页签 menuBlank新窗口）';
COMMENT ON COLUMN sys_menu.menu_type    IS'菜单类型（M目录 C菜单 F按钮）';
COMMENT ON COLUMN sys_menu.visible      IS'菜单状态（0显示 1隐藏）';
COMMENT ON COLUMN sys_menu.perms        IS'权限标识';
COMMENT ON COLUMN sys_menu.icon         IS'菜单图标';
COMMENT ON COLUMN sys_menu.create_by    IS'创建者';
COMMENT ON COLUMN sys_menu.create_time  IS'创建时间';
COMMENT ON COLUMN sys_menu.update_by    IS'更新者';
COMMENT ON COLUMN sys_menu.update_time  IS'更新时间';
COMMENT ON COLUMN sys_menu.remark       IS'备注';

-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------

insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('4', '硬件监控', '0', '1', '#', '', 'M', '0', '', 'glyphicon glyphicon-hdd',         'admin','硬件监控目录');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('5', '网络监控', '0', '2', '#', '', 'M', '0', '', 'glyphicon glyphicon-globe',         'admin','网络监控目录');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('6', '批次监控', '0', '3', '#', '', 'M', '0', '', 'glyphicon glyphicon-facetime-video',         'admin','批次监控目录');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('7', '软件监控', '0', '4', '#', '', 'M', '0', '', 'glyphicon glyphicon-briefcase',         'admin','软件监控目录');


insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('400',  '服务器基本信息', '4', '1', '/hardware/basic',       '', 'C', '0', '',      '#', 'admin',  '硬件监控菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('401',  '硬盘信息', '4', '2', '/hardware/disk',       '', 'C', '0', '',      '#', 'admin',  '硬件监控菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('402',  'CPU信息', '4', '3', '/hardware/cpu',       '', 'C', '0', '',      '#', 'admin',  '硬件监控菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('403',  'I/O信息', '4', '4', '/hardware/io',       '', 'C', '0', '',      '#', 'admin',  '硬件监控菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('404',  '内存信息', '4', '5', '/hardware/mem',       '', 'C', '0', '',      '#', 'admin',  '硬件监控菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('405',  '拓扑图', '4', '6', '/hardware/topological',       '', 'C', '0', '',      '#', 'admin',  '硬件监控菜单');

insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('500',  '网络连接', '5', '1', '/network/connect',       '', 'C', '0', '',      '#', 'admin',  '网络监控菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('501',  '丢包', '5', '2', '/network/packetloss',       '', 'C', '0', '',      '#', 'admin',  '网络监控菜单');

insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('600',  '文件接收', '6', '1', '/batch/file/receive',       '', 'C', '0', '',      '#', 'admin',  '批次监控菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('601',  '数据加载', '6', '2', '/batch/data/load',       '', 'C', '0', '',      '#', 'admin',  '批次监控菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('602',  '数据计算', '6', '3', '/batch/data/calculate',       '', 'C', '0', '',      '#', 'admin',  '批次监控菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('603',  '数据输出', '6', '4', '/batch/data/output',       '', 'C', '0', '',      '#', 'admin',  '批次监控菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('604',  '数据备份', '6', '5', '/batch/data/backup',       '', 'C', '0', '',      '#', 'admin',  '批次监控菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('605',  '批次流程', '6', '6', '/batch/flow',       '', 'C', '0', '',      '#', 'admin',  '批次监控菜单');

insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('700',  '基本信息', '7', '1', '/software/basic/info',       '', 'C', '0', '',      '#', 'admin',  '软件监控菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('701',  '软件状态', '7', '2', '/software/status',       '', 'C', '0', '',      '#', 'admin',  '软件监控菜单');




/*
-- 一级菜单
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1', '系统管理', '0', '1', '#', '', 'M', '1', '', 'fa fa-gear',         'admin','系统管理目录');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('2', '系统监控', '0', '2', '#', '', 'M', '1', '', 'fa fa-video-camera', 'admin',  '系统监控目录');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('3', '系统工具', '0', '3', '#', '', 'M', '1', '', 'fa fa-bars',         'admin',  '系统工具目录');
-- 二级菜单
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('100',  '用户管理', '1', '1', '/system/user',          '', 'C', '0', 'system:user:view',         '#', 'admin',  '用户管理菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('101',  '角色管理', '1', '2', '/system/role',          '', 'C', '0', 'system:role:view',         '#', 'admin',  '角色管理菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('102',  '菜单管理', '1', '3', '/system/menu',          '', 'C', '0', 'system:menu:view',         '#', 'admin',  '菜单管理菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('103',  '部门管理', '1', '4', '/system/dept',          '', 'C', '0', 'system:dept:view',         '#', 'admin',  '部门管理菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('104',  '岗位管理', '1', '5', '/system/post',          '', 'C', '0', 'system:post:view',         '#', 'admin',  '岗位管理菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('105',  '字典管理', '1', '6', '/system/dict',          '', 'C', '0', 'system:dict:view',         '#', 'admin',  '字典管理菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('106',  '参数设置', '1', '7', '/system/config',        '', 'C', '0', 'system:config:view',       '#', 'admin',  '参数设置菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('107',  '通知公告', '1', '8', '/system/notice',        '', 'C', '0', 'system:notice:view',       '#', 'admin',  '通知公告菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('108',  '日志管理', '1', '9', '#',                     '', 'M', '0', '',                         '#', 'admin',  '日志管理菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('109',  '在线用户', '2', '1', '/monitor/online',       '', 'C', '0', 'monitor:online:view',      '#', 'admin',  '在线用户菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('110',  '定时任务', '2', '2', '/monitor/job',          '', 'C', '0', 'monitor:job:view',         '#', 'admin',  '定时任务菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('111',  '数据监控', '2', '3', '/monitor/data',         '', 'C', '0', 'monitor:data:view',        '#', 'admin',  '数据监控菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('112',  '服务监控', '2', '3', '/monitor/server',       '', 'C', '0', 'monitor:server:view',      '#', 'admin',  '服务监控菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('113',  '表单构建', '3', '1', '/tool/build',           '', 'C', '0', 'tool:build:view',          '#', 'admin',  '表单构建菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('114',  '代码生成', '3', '2', '/tool/gen',             '', 'C', '0', 'tool:gen:view',            '#', 'admin',  '代码生成菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('115',  '系统接口', '3', '3', '/tool/swagger',         '', 'C', '0', 'tool:swagger:view',        '#', 'admin',  '系统接口菜单');
-- 三级菜单
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('500',  '操作日志', '108', '1', '/monitor/operlog',    '', 'C', '0', 'monitor:operlog:view',     '#', 'admin',  '操作日志菜单');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('501',  '登录日志', '108', '2', '/monitor/logininfor', '', 'C', '0', 'monitor:logininfor:view',  '#', 'admin',  '登录日志菜单');
-- 用户管理按钮
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1000', '用户查询', '100', '1',  '#', '',  'F', '0', 'system:user:list',        '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1001', '用户新增', '100', '2',  '#', '',  'F', '0', 'system:user:add',         '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1002', '用户修改', '100', '3',  '#', '',  'F', '0', 'system:user:edit',        '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1003', '用户删除', '100', '4',  '#', '',  'F', '0', 'system:user:remove',      '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1004', '用户导出', '100', '5',  '#', '',  'F', '0', 'system:user:export',      '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1005', '用户导入', '100', '6',  '#', '',  'F', '0', 'system:user:import',      '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1006', '重置密码', '100', '7',  '#', '',  'F', '0', 'system:user:resetPwd',    '#', 'admin',  '');
-- 角色管理按钮
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1007', '角色查询', '101', '1',  '#', '',  'F', '0', 'system:role:list',        '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1008', '角色新增', '101', '2',  '#', '',  'F', '0', 'system:role:add',         '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1009', '角色修改', '101', '3',  '#', '',  'F', '0', 'system:role:edit',        '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1010', '角色删除', '101', '4',  '#', '',  'F', '0', 'system:role:remove',      '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1011', '角色导出', '101', '5',  '#', '',  'F', '0', 'system:role:export',      '#', 'admin',  '');
-- 菜单管理按钮
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1012', '菜单查询', '102', '1',  '#', '',  'F', '0', 'system:menu:list',        '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1013', '菜单新增', '102', '2',  '#', '',  'F', '0', 'system:menu:add',         '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1014', '菜单修改', '102', '3',  '#', '',  'F', '0', 'system:menu:edit',        '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1015', '菜单删除', '102', '4',  '#', '',  'F', '0', 'system:menu:remove',      '#', 'admin',  '');
-- 部门管理按钮
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1016', '部门查询', '103', '1',  '#', '',  'F', '0', 'system:dept:list',        '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1017', '部门新增', '103', '2',  '#', '',  'F', '0', 'system:dept:add',         '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1018', '部门修改', '103', '3',  '#', '',  'F', '0', 'system:dept:edit',        '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1019', '部门删除', '103', '4',  '#', '',  'F', '0', 'system:dept:remove',      '#', 'admin',  '');
-- 岗位管理按钮
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1020', '岗位查询', '104', '1',  '#', '',  'F', '0', 'system:post:list',        '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1021', '岗位新增', '104', '2',  '#', '',  'F', '0', 'system:post:add',         '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1022', '岗位修改', '104', '3',  '#', '',  'F', '0', 'system:post:edit',        '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1023', '岗位删除', '104', '4',  '#', '',  'F', '0', 'system:post:remove',      '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1024', '岗位导出', '104', '5',  '#', '',  'F', '0', 'system:post:export',      '#', 'admin',  '');
-- 字典管理按钮
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1025', '字典查询', '105', '1', '#', '',  'F', '0', 'system:dict:list',         '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1026', '字典新增', '105', '2', '#', '',  'F', '0', 'system:dict:add',          '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1027', '字典修改', '105', '3', '#', '',  'F', '0', 'system:dict:edit',         '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1028', '字典删除', '105', '4', '#', '',  'F', '0', 'system:dict:remove',       '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1029', '字典导出', '105', '5', '#', '',  'F', '0', 'system:dict:export',       '#', 'admin',  '');
-- 参数设置按钮
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1030', '参数查询', '106', '1', '#', '',  'F', '0', 'system:config:list',      '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1031', '参数新增', '106', '2', '#', '',  'F', '0', 'system:config:add',       '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1032', '参数修改', '106', '3', '#', '',  'F', '0', 'system:config:edit',      '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1033', '参数删除', '106', '4', '#', '',  'F', '0', 'system:config:remove',    '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1034', '参数导出', '106', '5', '#', '',  'F', '0', 'system:config:export',    '#', 'admin',  '');
-- 通知公告按钮
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1035', '公告查询', '107', '1', '#', '',  'F', '0', 'system:notice:list',      '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1036', '公告新增', '107', '2', '#', '',  'F', '0', 'system:notice:add',       '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1037', '公告修改', '107', '3', '#', '',  'F', '0', 'system:notice:edit',      '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1038', '公告删除', '107', '4', '#', '',  'F', '0', 'system:notice:remove',    '#', 'admin',  '');
-- 操作日志按钮
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1039', '操作查询', '500', '1', '#', '',  'F', '0', 'monitor:operlog:list',    '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1040', '操作删除', '500', '2', '#', '',  'F', '0', 'monitor:operlog:remove',  '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1041', '详细信息', '500', '3', '#', '',  'F', '0', 'monitor:operlog:detail',  '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1042', '日志导出', '500', '4', '#', '',  'F', '0', 'monitor:operlog:export',  '#', 'admin',  '');
-- 登录日志按钮
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1043', '登录查询', '501', '1', '#', '',  'F', '0', 'monitor:logininfor:list',         '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1044', '登录删除', '501', '2', '#', '',  'F', '0', 'monitor:logininfor:remove',       '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1045', '日志导出', '501', '3', '#', '',  'F', '0', 'monitor:logininfor:export',       '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1046', '账户解锁', '501', '4', '#', '',  'F', '0', 'monitor:logininfor:unlock',       '#', 'admin',  '');
-- 在线用户按钮
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1047', '在线查询', '109', '1', '#', '',  'F', '0', 'monitor:online:list',             '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1048', '批量强退', '109', '2', '#', '',  'F', '0', 'monitor:online:batchForceLogout', '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1049', '单条强退', '109', '3', '#', '',  'F', '0', 'monitor:online:forceLogout',      '#', 'admin',  '');
-- 定时任务按钮
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1050', '任务查询', '110', '1', '#', '',  'F', '0', 'monitor:job:list',                '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1051', '任务新增', '110', '2', '#', '',  'F', '0', 'monitor:job:add',                 '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1052', '任务修改', '110', '3', '#', '',  'F', '0', 'monitor:job:edit',                '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1053', '任务删除', '110', '4', '#', '',  'F', '0', 'monitor:job:remove',              '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1054', '状态修改', '110', '5', '#', '',  'F', '0', 'monitor:job:changeStatus',        '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1055', '任务详细', '110', '6', '#', '',  'F', '0', 'monitor:job:detail',              '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1056', '任务导出', '110', '7', '#', '',  'F', '0', 'monitor:job:export',              '#', 'admin',  '');
-- 代码生成按钮
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1057', '生成查询', '114', '1', '#', '',  'F', '0', 'tool:gen:list',     '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1058', '生成修改', '114', '2', '#', '',  'F', '0', 'tool:gen:edit',     '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1059', '生成删除', '114', '3', '#', '',  'F', '0', 'tool:gen:remove',   '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1060', '预览代码', '114', '4', '#', '',  'F', '0', 'tool:gen:preview',  '#', 'admin',  '');
insert into sys_menu(menu_id,menu_name,parent_id ,order_num ,url,target ,menu_type ,visible,perms,icon,create_by,remark) values('1061', '生成代码', '114', '5', '#', '',  'F', '0', 'tool:gen:code',     '#', 'admin',  '');

*/
