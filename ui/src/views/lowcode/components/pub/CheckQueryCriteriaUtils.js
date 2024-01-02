var LexicalParser = function() {};
LexicalParser.prototype = {
    rOperand: /^\d+$/,
    parse: function(code) {
        var m, lexical, arRslt = [{
            type: '^',
            value: '',
            index: 0
        }];

        if (this.hasChineseInpunt(code)){
            error({
                code: -6,
                message: '存在中文字符！',
                index: index
            });
        }
        var token, index,
            rLexical = /\d+|and|or|\(|\)|（|）|\S+/g;
        while (m = rLexical.exec(code)) {
            token = m[0];
            index = m.index;
            lexical = {
                value: token,
                index: index
            };
            // console.log(index, token);
            if (token == 'and') {
                lexical.type = 'operator';
            } else if (token == 'or') {
                lexical.type = 'operator';
            } else if (token == '(' || token == '（') {
                lexical.type = 'limit_start';
            } else if (token == ')' || token == '）') {
                lexical.type = 'limit_end';
            } else if (this.rOperand.test(token)) {
                if (this.operands && this.operands.length && !~this.operands.indexOf(token)) {
                    error({
                        code: -1,
                        message: '存在错误的编号！',
                        index: index
                    });
                }
                lexical.type = 'operand';
            } else {
                error({
                    code: -2,
                    message: '存在错误的字符！',
                    index: index
                });
            }
            arRslt.push(lexical);
        }
        arRslt.push({
            type: '$',
            value: '',
            index: code.length
        });
        return arRslt;
    },
    setValidOperands: function(operands) {
        this.operands = operands;
    },
    hasChineseInpunt:function(code){
    //匹配这些中文标点符号 。 ？ ！ ， 、 ； ： “ ” ‘ ' （ ） 《 》 〈 〉 【 】 『 』 「 」 ﹃ ﹄ 〔 〕 … — ～ ﹏ ￥
        var reg = /[\u3002|\uff1f|\uff01|\uff0c|\u3001|\uff1b|\uff1a|\u201c|\u201d|\u2018|\u2019|\uff08|\uff09|\u300a|\u300b|\u3008|\u3009|\u3010|\u3011|\u300e|\u300f|\u300c|\u300d|\ufe43|\ufe44|\u3014|\u3015|\u2026|\u2014|\uff5e|\ufe4f|\uffe5]/;
       return reg.test(code);
    }
};

var SyntaxParser = function() {};
SyntaxParser.prototype = {
    parse: function(lexicals) {
        var stack = [0],
            prev,
            node,
            errMsg,
            ast;
        lexicals.forEach(function(lexical, index) {
            if (index == 0) {
                return;
            }
            prev = lexicals[index - 1];
            errMsg = {
                code: -3,
                message: '公式错误',
                index: lexical.index
            };

            switch (prev.type) {
                case 'operand':
                    switch (lexical.type) {
                        case 'operator':
                            if (!ast.fall && ast.type == 'operator' && lexical.value != ast.value) {
                                error({
                                    code: -5,
                                    message: 'and与or需要用括号分隔',
                                    index: lexical.index
                                });
                            }
                            lexical.left = ast;
                            ast = stack[stack.length - 1] = lexical;
                            break;
                        case 'limit_end':
                            node = stack.pop();
                            if(stack.length>0) {
                                ast = stack[stack.length - 1];
                            }else{
                                error({
                                    code: -5,
                                    message: '缺失左括号',
                                    index: lexical.index
                                });
                            }
                            if (ast === 0) {
                                ast = stack[stack.length - 1] = node;
                                ast.fall = true;
                            } else if (ast.type == 'operator') {
                                ast.right = node;
                            } else {
                                error(errMsg);
                            }
                            break;
                        case '$':
                            break;
                        default:
                            error(errMsg);
                    };
                    break;
                case 'operator':
                    switch (lexical.type) {
                        case 'operand':
                            // ast = prev;
                            prev.right = lexical;
                            break;
                        case 'limit_start':
                            stack.push(0);
                            break;
                        default:
                            error(errMsg);
                    };
                    break;
                case 'limit_start':
                    switch (lexical.type) {
                        case 'operand':
                            ast = stack[stack.length - 1] = lexical;
                            break;
                        case 'limit_start':
                            stack.push(0);
                            break;
                        default:
                            error(errMsg);
                    };
                    break;
                case 'limit_end':
                    switch (lexical.type) {
                        case 'operator':
                            if (!ast.fall && ast.type == 'operator' && lexical.value != ast.value) {
                                error({
                                    code: -5,
                                    message: '缺失左括号',
                                    index: lexical.index
                                });
                            }
                            lexical.left = ast;
                            ast = stack[stack.length - 1] = lexical;
                            break;
                        case 'limit_end':
                            node = stack.pop();
                            if(stack.length>0) {
                                ast = stack[stack.length - 1];
                            }else{
                                error({
                                    code: -5,
                                    message: '缺失左括号',
                                    index: lexical.index
                                });
                            }
                            if (ast === 0) {
                                ast = stack[stack.length - 1] = node;
                                ast.fall = true;
                            } else if (ast.type == 'operator') {
                                ast.right = node;
                            } else {
                                error(errMsg);
                            }
                            break;
                        case '$':
                            break;
                        default:
                            error(errMsg);
                    };
                    break;
                case '^':
                    switch (lexical.type) {
                        case 'operand':
                            if (stack[stack.length - 1] === 0) {
                                ast = stack[stack.length - 1] = lexical;
                            }
                            break;
                        case 'limit_start':
                            stack.push(0);
                            break;
                        case '$':
                            break;
                        default:
                            error(errMsg);
                    };
                    break;
            }
        });

        if (stack.length != 1) {
            error({
                code: -4,
                message: '缺失右括号'
            });
        } else {
            return this.ast = ast;
        }
    },
    getString: function() {
        var ast = this.ast,
            arRslt = [];
        if (!ast) {
            return '';
        }
        return this._ldr(ast).map(function(token) {
            return token == 'and' || token == 'or' ? ' ' + token + ' ' : token;
        }).join('');
    },
    _ldr: function(ast, operator) {
        if (!ast) return [];
        var expression = this._ldr(ast.left, ast.type == 'operator' ? ast.value : null).concat([ast.value], this._ldr(ast.right, ast.type == 'operator' ? ast.value : null));
        return operator && ast.type == 'operator' && ast.value != operator ? ['('].concat(expression, [')']) : expression;
    }
};

function error(json) {
    throw new Error(JSON.stringify(json));
}

/*************************************************************************************************/

class CheckQueryCriteriaUtils {
	static check(code,list) {
	   console.log('Code: ', code);
	   try {
	       var lp = new LexicalParser();
	       lp.setValidOperands(list);
	       var lexicals = lp.parse(code);
	       if (lexicals) {
	           var sp = new SyntaxParser(),
	               ast = sp.parse(lexicals);
	       }
	   } catch (ex) {
		   return JSON.parse(ex.message)
	       // console.log(JSON.parse(ex.message));
	   }
	   return ""
	}
}

export default CheckQueryCriteriaUtils;

//
////错误的语法
//[
//    '1 and 10',
//    '1 * 2',
//    '1 and or 2',
//    '1 and 2 and ((3)',
//    '1 and 2 or (3)',
//    'and',
//    'and 2',
//    '1 and',
//    '1 2',
//    '1 (and) 2',
//    '1 an d 2',
//    '()',
//].forEach(test);
//
////正确的语法
//[
//    '',
//    ' ((((2)))) ',
//    '1and(2or3or(4and5and(6)))and(7and(8))',
//    '1 and (((7) and 8) or 2 or 3 or ( 4 and 5 and (6))) and (7 and (8))',
//    '1 and (((2)) or (((3)) and (((((4)))) or ((((5))))) and ((((6))))) or ((((7)))) or ((((8)))))',
//    '1 and (2 or( 3 and( 4 or(5 and( 6or(7 and(8 or 9))))))) ',
//    '(((((1))))) and (((2 or 3 ) or 4) or 5) and ((((((6))))))'
//].forEach(test);
