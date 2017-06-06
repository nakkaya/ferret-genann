(native-header "ferret-genann/genann/genann.h")

(defobject genann_o
  (data "genann *_network;"
        "number_t num_inputs;"
        "number_t num_hidden_layers;"
        "number_t num_hidden;"
        "number_t _num_outputs;")
  
  (equals "return obj<boolean>(_network == o.cast<genann_o>()->network());")
  (stream_console "runtime::print(\"genann<\");
                   runtime::print(num_inputs);
                   runtime::print(\" \");
                   runtime::print(num_hidden_layers);
                   runtime::print(\" \");
                   runtime::print(num_hidden);
                   runtime::print(\" \");
                   runtime::print(_num_outputs);
                   runtime::print(\">\");
                   return nil();")
  (fns
   ("explicit genann_o(number_t i, number_t hl, number_t h, number_t o) 
                  : num_inputs(i), num_hidden_layers(hl), num_hidden(h), _num_outputs(o){ 
       _network = genann_init(num_inputs, num_hidden_layers, num_hidden, _num_outputs);
     }")
   ("explicit genann_o(genann *ann) : _network(ann){ 
      num_inputs = _network->inputs;
      num_hidden_layers = _network->hidden_layers;
      num_hidden = _network->hidden;
      _num_outputs = _network->outputs;
     }")
   ("genann *network() const { return _network; }")
   ("number_t num_outputs() const { return _num_outputs; }")
   ("~genann_o(){ genann_free(_network); }"))
  (force-type))

(defn init [input hidden-layers hidden outputs]
  "__result  = obj<genann_o>(number::to<number_t>(input), 
                             number::to<number_t>(hidden_layers), 
                             number::to<number_t>(hidden), 
                             number::to<number_t>(outputs));")

(defn train [nn in out learning-rate]
  "genann *ann = nn.cast<genann_o>()->network();

   std::vector<double> inputs;
   for(auto it : runtime::range(in)) inputs.push_back(number::to<real_t>(it));

   std::vector<double> outputs;
   for(auto it : runtime::range(out)) outputs.push_back(number::to<real_t>(it));

   genann_train(ann, inputs.data(), outputs.data(), number::to<real_t>(learning_rate));")

(defn train-array [nn in out learning-rate]
  "genann *ann = nn.cast<genann_o>()->network();
   double *inputs = pointer::to_pointer<double>(in);
   double *outputs = pointer::to_pointer<double>(out);
   genann_train(ann, inputs, outputs, number::to<real_t>(learning_rate));")

(defn train-vector [nn in out learning-rate]
  "genann *ann = nn.cast<genann_o>()->network();
   genann_train(ann, 
                value<std::vector<double>>::to_value(in).data(),
                value<std::vector<double>>::to_value(out).data(),
                number::to<real_t>(learning_rate));")

(defn run [nn in]
  "genann *ann = nn.cast<genann_o>()->network();

   std::vector<double> inputs;
   for(auto it : runtime::range(in)) inputs.push_back(number::to<real_t>(it));

   double const *ret = genann_run(ann, inputs.data());
   number_t size = nn.cast<genann_o>()->num_outputs();
   for(number_t i = --size; i >= 0; i--)
     __result = runtime::cons(obj<number>(ret[i]),__result);")

(defn run-vector [nn in]
  "genann *ann = nn.cast<genann_o>()->network();

   double const *ret = genann_run(ann, value<std::vector<double>>::to_value(in).data());
   number_t size = nn.cast<genann_o>()->num_outputs();
   for(number_t i = --size; i >= 0; i--)
     __result = runtime::cons(obj<number>(ret[i]),__result);")

(defn write [nn f]
  "genann *ann = nn.cast<genann_o>()->network();
   FILE *file = fopen(string::to<std::string>(f).c_str(), \"w\");

   if (file == NULL)
     return nil();
 
   genann_write(ann,file);
   __result = obj<boolean>(true);")

(defn read [f]
  "FILE *file = fopen(string::to<std::string>(f).c_str(), \"r\");

   if (file == NULL)
     return nil();

   __result  = obj<genann_o>(genann_read(file));")
