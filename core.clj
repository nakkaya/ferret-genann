(native-header "ferret-genann/genann/genann.h")

(defobject genann_o "ferret-genann/genann_o.h")

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
