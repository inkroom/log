package cn.inkroom.tools.log.server.appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @author 墨盒
 * @Deate 18-8-9
 */
public class EveryDayRollingFileAppender extends FileAppender {

    private SimpleDateFormat format;
    private String fileName;

    private Level level;

    private static final String SEPERTOR = "-";
    private String regex = "\\{([^\\{\\}]+)\\}";//

    private long next;

    private Date now;

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public EveryDayRollingFileAppender() {
        now = new Date();
        level = Level.WARN;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    //TODO 18-6-16 该方法被调用了两次
    @Override
    public void activateOptions() {
        Pattern pattern = Pattern.compile(regex);


        Matcher matcher = pattern.matcher(fileName);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            String temp = matcher.group(1);

            builder.append(temp).append(SEPERTOR);

        }
        if (builder.length() != 0) {
            format = new SimpleDateFormat(builder.deleteCharAt(builder.length() - 1).toString());
            changeFile();
            setNextTime();
        } else {
            format = null;
            try {
                setFile(fileName, getAppend(), getBufferedIO(), getBufferSize());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void changeFile() {
        if (format != null) {
            try {
                setFile(createFile(), getAppend(), getBufferedIO(), getBufferSize());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void setNextTime() {
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.DAY_OF_YEAR, 1);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        next = c.getTimeInMillis();

        System.out.println(new Date(next));
    }


    @Override
    protected void subAppend(LoggingEvent event) {
        if (event.getLevel().getSyslogEquivalent() <= level.getSyslogEquivalent()) {
            long n = System.currentTimeMillis();
            if (n >= next) {
                now.setTime(n);

                changeFile();

                setNextTime();


            }
            super.subAppend(event);
        }
    }

    public String createFile() {
        String file = format.format(now);

        String s[] = file.split(SEPERTOR);

        String temp = fileName;
        for (int i = 0; i < s.length; i++) {
            temp = temp.replaceFirst(regex, s[i]);
        }


        File f = new File(temp);
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        else if (f.exists())
            f.delete();

        return f.getAbsolutePath();

    }
}