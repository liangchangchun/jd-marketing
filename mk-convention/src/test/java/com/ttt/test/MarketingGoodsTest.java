package com.ttt.test;

import java.math.BigDecimal;

public class MarketingGoodsTest {

public static void main(String[] args) {
	
BigDecimal decimal = new BigDecimal("1.12345");
        System.out.println(decimal);
        BigDecimal setScale = decimal.setScale(4,BigDecimal.ROUND_HALF_DOWN);
        System.out.println(setScale);
        BigDecimal setScale1 = decimal.setScale(4,BigDecimal.ROUND_HALF_UP);
        System.out.println(setScale1);
        /*
        salePrice：188   商品售价
        redPackage：0 红包抵扣额度
        costPrice：101.6 商品成本价
        poolRoportion：0.15 积分池比例
        inputTax：0.17 进项税率
        outputTax：0.17  销项税率
        operatingCosts：0.06 运营成本占比
        */
        BigDecimal salePrice = new BigDecimal("188");
        BigDecimal redPackage = new BigDecimal("0");
        BigDecimal costPrice = new BigDecimal("101.6");
        costPrice.setScale(4, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal outputTax = new BigDecimal("0.17");
        outputTax.setScale(4, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal poolRoportion = new BigDecimal("0.15");
        poolRoportion.setScale(4, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal inputTax = new BigDecimal("0.17");
        inputTax.setScale(4, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal operatingCosts = new BigDecimal("0.06");
        operatingCosts.setScale(4, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal redp = getRedpackNet( salePrice , redPackage, costPrice, outputTax, poolRoportion, inputTax, operatingCosts);
        System.out.println("redpack 抵扣红包商品净利率 :" + redp.doubleValue());
        
        
        BigDecimal maoNet = getMaoNet( redp , salePrice, redPackage);
        System.out.println("maoNet 毛利:" + maoNet.doubleValue());
	}

	public static BigDecimal getMaoNet(BigDecimal redpack,BigDecimal salePrice,BigDecimal redPackage) {
		return mul(redpack, sub(salePrice,redPackage));
	}

	public static BigDecimal getRedpackNet(BigDecimal salePrice ,BigDecimal redPackage,BigDecimal costPrice,BigDecimal outputTax,BigDecimal poolRoportion,BigDecimal inputTax,BigDecimal operatingCosts) {
		BigDecimal redpack = new BigDecimal(0);
		BigDecimal base = new BigDecimal("100");
		BigDecimal one = new BigDecimal("1");
		BigDecimal a = sub(salePrice,div(redPackage,base,4));
		BigDecimal b = sub(a,costPrice);
		BigDecimal c = div(outputTax,add(one,outputTax),4);
		BigDecimal d = mul(costPrice,div(inputTax,add(one,inputTax),4));
		BigDecimal e = add(operatingCosts,poolRoportion);
		BigDecimal f = sub(mul(a,c),d);
		BigDecimal g = sub(b,f);
		BigDecimal h = mul(a,e);
		redpack = div(sub(g,h),a,4);
		return redpack;
	}
	
	//减
	public static BigDecimal sub(BigDecimal v1,BigDecimal v2){ 
      return v1.subtract(v2); 
	} 

  public static BigDecimal add(BigDecimal v1,BigDecimal v2){ 
	return v1.add(v2); 
 }

 public static BigDecimal mul(BigDecimal v1,BigDecimal v2){ 
	return v1.multiply(v2); 
 } 

  public static BigDecimal div(BigDecimal v1,BigDecimal v2,int scale){
	if( scale<0 ){
		throw new IllegalArgumentException("The scale must be a positive integer or zero");
	}
	return v1.divide(v2,scale,BigDecimal.ROUND_HALF_UP);
  }

}
