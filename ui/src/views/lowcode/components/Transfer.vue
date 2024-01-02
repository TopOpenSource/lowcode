<template>
	<div>
		<el-transfer v-model="inputValue" :data="data" @right-check-change = "checkChange" target-order ="push" :titles="titles" :props="props">
			<el-button  @click="valueUp"  :disabled = "upActive"  slot="right-footer" size="small" type="primary" icon="el-icon-arrow-up"></el-button>
			<el-button  @click="valueDown" :disabled = "downActive"  slot="right-footer" size="small" type="primary" icon="el-icon-arrow-down"></el-button>
		</el-transfer>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				inputValue:[],
				checkKey:[],
			}
		},
		props: {
			titles:null,
			value: {
				type: Array,
				default: [],
			},
			data: {
				type: Array,
				default: [],
			},
			props:null
		},
		computed: {
			upActive() {
				return !(this.checkKey.length ==1&&this.inputValue.indexOf(this.checkKey[0])!=0)

			},
			downActive() {
				return !(this.checkKey.length ==1&&this.inputValue.indexOf(this.checkKey[0])!=(this.inputValue.length-1))
			}
		},
		watch:{
			inputValue: function (n, o) {
			  this.$emit("input", n);
			},
			value:function(newValue,oldVal){
			  if(this.value==undefined){
			    this.inputValue='';
			  }else{
			    this.inputValue = this.value;
			  }
			}
		},
		methods: {
			valueUp(){
				let arr = this.inputValue
				if(this.checkKey.length == 1){
					let index = arr.indexOf(this.checkKey[0])
					arr.splice(index-1,0,arr[index])
					arr.splice(index+1, 1);
					this.inputValue = arr
				}
			},
			valueDown(){
				let arr = this.inputValue
				if(this.checkKey.length == 1){
					let index = arr.indexOf(this.checkKey[0])
					arr.splice(index+2,0,arr[index])
					arr.splice(index, 1);
					this.inputValue = arr
				}
			},

			checkChange(val){
				this.checkKey = val
			}
		}
	}
</script>

<style>
</style>
