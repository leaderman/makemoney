package io.github.leaderman.makemoney.hustle.eastmoney.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import io.github.leaderman.makemoney.hustle.eastmoney.domain.entity.FundPriceEntity;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.view.FundBucketPriceView;

@Mapper
public interface FundPriceMapper extends BaseMapper<FundPriceEntity> {
  @Select("""
      SELECT
        code,
        FROM_UNIXTIME(UNIX_TIMESTAMP(created_at) - (UNIX_TIMESTAMP(created_at) % #{size})) AS bucketed_at,
        AVG(price) AS price
      FROM
        fund_price
      WHERE
        code = #{code}
        AND created_at BETWEEN #{start} AND #{end}
      GROUP BY
        code,
        bucketed_at
      ORDER BY
        bucketed_at DESC
      """)
  List<FundBucketPriceView> bucket(@Param("code") String code, @Param("size") int size,
      @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
