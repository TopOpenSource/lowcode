import moment from 'moment'

/**
 * 获取昨天的时间
 * @param nowDate
 * @returns {moment.Moment}
 */
export function getYesterday(nowDate) {
  if (nowDate instanceof Date) {
    // let year,month,day
    // nowDate.setTime(nowDate.getTime()-24*60*60*1000);
    // [year,month,day] = [nowDate.getFullYear(),nowDate.getMonth(),nowDate.getDate()]
    // let yesterday = new Date(year,month,day,nowDate.getHours(),nowDate.getMinutes(), nowDate.getSeconds())
    let yesterday = moment(nowDate).add(-1, 'days')
    return yesterday
  }
}

/**
 * 日期格式化
 * @param val
 * @param pattern
 * @returns {string}
 */
export function formatDate(val, pattern = 'yyyy-MM-DD HH:mm:ss') {
  return moment(val).format(pattern)
}

/**
 * 获取时间间隔
 * @param start
 * @param end
 * @param count
 * @returns {number}
 */
export function getStep(start, end, count = 100) {
  let startNum = moment(start).valueOf() / 1000
  let endNum = moment(end).valueOf() / 1000
  let step = parseInt((endNum - startNum) / count);
  return step;
}
