package com.skangyam.hadoop.mapreduce.UsingNormalMapChain;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class NormalJobChain extends Configured implements Tool {

	public int run(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.printf("Usage: %s [generic options] "
					+ "<input1 dir> <output dir>\n",
					getClass().getSimpleName());
			return -1;
		}
		
		Path temp = new Path("/user/skangyam/tmp");
		
		/* Job1 */
		Job job1 = new Job(getConf());
		job1.setJarByClass(NormalJobChain.class);
		job1.setJobName(this.getClass().getName());
		
		FileInputFormat.addInputPath(job1, new Path(args[0]));
		FileOutputFormat.setOutputPath(job1, temp);
		
		job1.setMapperClass(MapperCAExtract.class);
		
		job1.setMapOutputKeyClass(Text.class);
		job1.setMapOutputValueClass(Text.class);
		job1.setNumReduceTasks(0);
		
		
		job1.waitForCompletion(true);
		
		//End of Job1
		
		/* Job2 */
		Job job2 = new Job(getConf());
		job2.setJarByClass(NormalJobChain.class);
        job2.setJobName(this.getClass().getName());
		
		FileInputFormat.addInputPath(job2, temp);
		FileOutputFormat.setOutputPath(job2, new Path(args[1]));
		
		job2.setMapperClass(MapperCalMaxSalForCA.class);
		
		job2.setMapOutputKeyClass(Text.class);
		job2.setMapOutputValueClass(Text.class);
		job2.setNumReduceTasks(0);
		
		
		if (job2.waitForCompletion(true)){
			FileSystem fs = FileSystem.get(getConf());
			fs.delete(temp,true);
			return 1;
		}
		
		return 0;
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new NormalJobChain() , args);
		System.exit(exitCode);

	}

}
