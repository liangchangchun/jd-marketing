package com.mk.convention.model;

import lombok.Data;


/**
 * @program: lovego-jd
 * @description: 京东统一下单接口
 * @author: Miaoxy
 * @create: 2018-05-08 14:10
 **/
@Data
public class OrderJdInfo {
    private  String thirdOrder;//必须 	第三方的订单单号
    private  String sku;/*须 	[{"skuId": 商品编号 , "num": 商品数量 ,"bNeedAnnex":true,
    "bNeedGift":true, "price":100, "yanbao":[{"skuId": 商 品 编号}]}](最高支持 50 种商品)
     bNeedAnnex 表示是否需要附件，默认为需要  bNeedGift 表示是否需要赠品，默认不要赠品，默认值为：false，
     如果需要增品 bNeedGift 请给 true,建议该参数都给 true,但如果实在不需要赠品可以给 false;
     price 表示透传价格，需要合同权限，接受价格权限，否则不允许传该值；一般不建议使用该字段。*/
    private  String name; //必须 	收货人
    private int   province;//	必须 	一级地址
    private int   city;//必须 	二级地址
    private int   county;//必须 	三级地址
    private int   town;//必须 	四级地址 (如果该地区有四级地址，则必须传递四级地址，没有四级地址则传 0)
    private String address;//必须 	详细地址
    private String zip;//非必须 	邮编
    private String phone;//非必须 	座机号
    private String mobile;//	必须 	手机号
    private String email;//	必须 	邮箱
    private String remark;//非必须 	备注（少于 100 字）
    private	int 	invoiceState;//必须 	开票方式(1 为随货开票，0 为订单预借，2 为集中开票 )
    private	int 	invoiceType;//必须 	1 普通发票 2 增值税发票 3 电子发票
    private	int 	selectedInvoiceTitle;//必须 	发票类型：4 个人，5 单位
    private	String 	companyName;//必须 	//发票抬头  (如果 selectedInvoiceTitle=5 则此字段必须)
    private	int 	invoiceContent;//必须 	1:明细，3：电脑配件，19:耗材，22：办公用品备注:若增值发票则只能选 1 明细
    private	int 	paymentType;//必须 	支付方式 (1：货到付款，2：邮局付款，4：余额支付，5：公司转账（公对公转账），7：网银钱包，101：金采支付)
    private 	int 	isUseBalance;//必须 	使用余额 paymentType=4 时，此值固定是 1 其他支付方式 0
    private	int 	submitState;//必须 	是否预占库存，0 是预占库存（需要调用确认订单接口），1 是不预占库存
    private	String 	invoiceName;//非必须 	增值票收票人姓名 备注：当 invoiceType=2 且 invoiceState=1 时则此字段必填
    private	String 	invoicePhone;//非必须 	增值票收票人电话 备注：当 invoiceType=2 且 invoiceState=1 时则此字段必填
    private 	int 	invoiceProvice;//非必须 	增值票收票人所在省(京东地址编码) 备注：当 invoiceType=2 且 invoiceState=1 时则此字段必填
    private	int 	invoiceCity;//非必须 	增值票收票人所在市(京东地址编码)  备注：当 invoiceType=2 且 invoiceState=1 时则此字段必填
    private	int 	invoiceCounty;//非必须 	增值票收票人所在区/县(京东地址编码) 备注：当 invoiceType=2 且 invoiceState=1 时则此字段必填
    private 	String 	invoiceAddress;//非必须 	增值票收票人所在地址   备注：当 invoiceType=2 且 invoiceState=1 时则此字段必填
    private	int doOrderPriceMode;/*下单价格模式 	0: 客户端订单价格快照不做验证对比，还是以京东价格正常下单;
                                            1:必需验证客户端订单价格快照，如果快照与京东价格不一致返回下单失败，需要更新商品价格后，重新下单;*/
    private  String orderPriceSnap;/*客户端订单价格快照 	Json 格式的数据，格式为:   [
                                                                                 {
                                                                                     "price":21.30, //商品价格 ,类型：BigDecimal
                                                                                      "skuId":123123 //商品编号,类型：long
                                                                                       },{
                                                                                            "price":99.55,
                                                                                             "skuId":22222
                                                                                            }
                                                                                        ]*/
    private int reservingDate;//大家电配送日期 	默认值为-1，0 表示当天，1 表示明天，2：表示后天; 如果为-1 表示不使用大家电预约日历
    private  int  installDate ;//大家电安装日期 	不支持默认按-1 处理，0 表示当天，1 表示明天，2：表示后天
    private  boolean needInstall;//大家电是否选择了安装 	是否选择了安装，默认为 true，选择了“暂缓安装”，此为必填项，必填值为 false。
    private  String promiseDate;//中小件配送预约日期 	格式：yyyy-MM-dd
    private  String promiseTimeRange;// 		中小件配送预约时间段 	时间段如： 9:00-15:00
    private  Integer promiseTimeRangeCode;//中小件预约时间段的标记
    private String reservedDateStr ;//大家电配送预约日期 	格式：yyyy-MM-dd
    private  String reservedTimeRange;//大家电配送预约时间段 	时间段如： 9:00-15:00
    private  String poNo;//采购单号 	长度范围【1-26】
    private  String regCode;//纳税人识别号 	开普票并要打印出来识别号时， 需传入该字段
    private  String customerName ;//结算单位 	单账号多点结算时， 根据该字段分别结算
}
