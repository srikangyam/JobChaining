package com.skangyam.hadoop.mapreduce.UsingNormalMapChain;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;



public class MapperCalMaxSalForCA extends Mapper<LongWritable, Text, Text, Text> {
	@Override
	protected void map(LongWritable key, Text value, Context context)
	          throws IOException, InterruptedException{
		String[] str = value.toString().split(",");
		int sal = Integer.parseInt(str[3].trim());
		if ((sal > 100000) && (sal <= 130000)){
			context.write(value, new Text());
		}
	}

}
