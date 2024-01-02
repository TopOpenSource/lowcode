<!-- 树状选择器 -->
<template>
	<el-popover ref="popover" placement="bottom-start" trigger="click" :disabled="disabled" @show="onShowPopover" @hide="onHidePopover">
		<el-input ref="selectInput" v-model="selectModel" placeholder="输入关键字" ></el-input>
		<el-tree 
			ref="tree" 
			class="select-tree" 
			highlight-current 
			:node-key="props.id"
			:style="`min-width: ${treeWidth}`" 
			:data="data" 
			:props="props" 
			:expand-on-click-node="false" 
			:filter-node-method="filterNode" 
			:default-expand-all="false" 
			@node-click="onClickNode">
		</el-tree>
		<el-input slot="reference" 
			ref="input" 
			:disabled="disabled"
			v-model="labelModel" 
			:style="`width: ${width}px`" 
			:placeholder="placeholder" 
			@mouseenter.native="inputHovering = true" 
			@mouseleave.native="inputHovering = false">
			<template slot="suffix">
				<i v-show="!showClose" :class="['el-select__caret', 'el-input__icon', 'el-icon-' + iconClass]"></i>
				<i v-if="showClose" class="el-select__caret el-input__icon el-icon-circle-close" @click="handleClearClick"></i>
			</template>
		</el-input>
	</el-popover>
</template>

<script>
	export default {
		name: 'SelectTree',
		props: {
			// 接收绑定参数
			value: String,
			// 输入框宽度
			width: String,
			// 选项数据
			options: {
				type: Array,
				required: true,
			},
			// 输入框占位符
			placeholder: {
				type: String,
				required: false,
				default: '请选择',
			},
			// 树节点配置选项
			props: {
				type: Object,
				required: false,
				default: () => ({
					id: 'id',
					parent: 'parent',
					value: 'value',
					label: 'label',
					children: 'children',
				}),
			},
			disabled:Boolean,
			clearable: Boolean,
		},
		// 设置绑定参数
//		model: {
//			prop: 'value',
//			event: 'selected',
//		},
		computed: {
			// 是否为树状结构数据
			dataType() {
				const jsonStr = JSON.stringify(this.options);
				return jsonStr.indexOf(this.props.children) !== -1;
			},
			// 若非树状结构，则转化为树状结构数据
			data() {
				return this.dataType ? this.options : this.switchTree();
			},

			showClose() {
				let hasValue = this.labelModel !== undefined && this.labelModel !== null && this.labelModel !== '';
				let criteria = this.clearable && this.inputHovering && hasValue && !this.disabled;
				return criteria;
			},

			iconClass() {
				return this.showStatus ? 'arrow-down' : 'arrow-up';
			},
		},
		watch: {
			labelModel(val) {
				if(!val) {
					this.valueModel = '';
				}
			},
			selectModel(val) {
				this.$refs.tree.filter(val);
			},
			value(val) {
				this.labelModel = this.queryTree(this.data, val);
				this.setCurrentKey(val);
			},
		},
		data() {
			return {
				// 树状菜单显示状态
				showStatus: false,
				// 菜单宽度
				treeWidth: 'auto',
				// 输入框显示值
				labelModel: '',
				//查询关键字
				selectModel:'',
				// 实际请求传值
				valueModel: '',

				inputHovering: false,
			};
		},
		created() {
			// 检测输入框原有值并显示对应 label
			if(this.value) {
				this.labelModel = this.queryTree(this.data, this.value);
				//设置选中
				this.setCurrentKey(this.value);
			}
			// 获取输入框宽度同步至树状菜单宽度
			this.$nextTick(() => {
				this.treeWidth = `${(this.width || this.$refs.input.$refs.input.clientWidth) - 24}px`;
			});
		},
		methods: {
			setCurrentKey(key){
				this.$nextTick(() => {
					this.$refs.tree.setCurrentKey(key);
				});
			},
			handleClearClick() {
				this.labelModel = "";
				this.valueModel = "";
				this.$refs.tree.setCurrentKey(null);
			},
			// 单击节点
			onClickNode(node) {
				this.labelModel = node[this.props.label];
				this.valueModel = node[this.props.value];
				this.onCloseTree();
			},
			// 偏平数组转化为树状层级结构
			switchTree() {
				return this.cleanChildren(this.buildTree(this.options));
			},
			// 隐藏树状菜单
			onCloseTree() {
				this.$refs.popover.showPopper = false;
			},
			// 显示时触发
			onShowPopover() {
				this.showStatus = true;
				this.$refs.tree.filter(false);
			},
			// 隐藏时触发
			onHidePopover() {
				this.showStatus = false;
				this.$emit('selected', this.valueModel);
				this.$emit('input', this.valueModel);
				this.$emit('change', this.valueModel);
			},
			// 树节点过滤方法
			filterNode(query, data) {
				if(!query) return true;
				return data[this.props.label].indexOf(query) !== -1;
			},
			// 搜索树状数据中的 ID
			queryTree(tree, id) {
				let stark = [];
				stark = stark.concat(tree);
				while(stark.length) {
					const temp = stark.shift();
					if(temp[this.props.children]) {
						stark = stark.concat(temp[this.props.children]);
					}
					if(temp[this.props.value] === id) {
						return temp[this.props.label];
					}
				}
				return '';
			},
			// 将一维的扁平数组转换为多层级对象
			buildTree(data) {
				let id = this.props.id;
				let pId = this.props.parent;
				let children = this.props.children;
				if(!data || !Array.isArray(data)) {
					return new Array();
				} 
				var treeMap = {};
				var roots = new Array();
				Array.from(data).forEach((item)=>{
					treeMap[item[id]] = item;
				});
				Array.from(data).forEach((item)=>{
					var parent = treeMap[item[pId]];
					if(parent != undefined && parent != null) {
						if(parent.children == undefined) {
							parent.children = [];
						}
						parent.children.push(item);
					} else {
						roots.push(item);
					}
				});
				return roots;
			},

			// 清除空 children项
			cleanChildren(data) {
				const fa = (list) => {
					list.map((e) => {
						if(e.children != undefined && e.children != null && e.children.length) {
							fa(e.children);
						} else {
							delete e.children;
						}
						return e;
					});
					return list;
				};
				return fa(data);
			},
		},
	};
</script>

<style>
	/*.el-input.el-input--suffix {
    cursor: pointer;
    overflow: hidden;
  }*/
	/*.el-input.el-input--suffix.rotate .el-input__suffix {
    transform: rotate(180deg);
  }*/
	
	.select-tree {
		max-height: 350px;
		overflow-y: scroll;
	}
	/* 菜单滚动条 */
	
	.select-tree::-webkit-scrollbar {
		z-index: 11;
		width: 6px;
	}
	
	.select-tree::-webkit-scrollbar-track,
	.select-tree::-webkit-scrollbar-corner {
		background: #fff;
	}
	
	.select-tree::-webkit-scrollbar-thumb {
		border-radius: 5px;
		width: 6px;
		background: #b4bccc;
	}
	
	.select-tree::-webkit-scrollbar-track-piece {
		background: #fff;
		width: 6px;
	}
	
	.select-tree .is-current .el-tree-node__label{
	    color: #409EFF;
	    font-weight: 700;
	}
	.select-tree .is-current .el-tree-node__children .el-tree-node__label{
	    color:#606266;
	    font-weight: normal;
	}
</style>